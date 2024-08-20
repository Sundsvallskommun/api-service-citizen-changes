package se.sundsvall.citizenchanges.service;

import static generated.se.sundsvall.messaging.MessageStatus.SENT;
import static se.sundsvall.citizenchanges.util.Constants.EMAIL_SENDER_NAME;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_EMAIL_SENDER;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_EMAIL_SUBJECT_AUTUMN;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_EMAIL_SUBJECT_SPRING;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_REPORT_EMAIL_SUBJECT_AUTUMN;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_REPORT_EMAIL_SUBJECT_SPRING;
import static se.sundsvall.citizenchanges.util.Constants.STATUS_FAILED;
import static se.sundsvall.citizenchanges.util.Constants.STATUS_NOT_SENT;
import static se.sundsvall.citizenchanges.util.Constants.STATUS_SENT;
import static se.sundsvall.citizenchanges.util.Constants.getProcessableSkolskjutsStatuses;
import static se.sundsvall.citizenchanges.util.DateUtil.getNextYear;
import static se.sundsvall.citizenchanges.util.DateUtil.getCurrentYear;
import static se.sundsvall.citizenchanges.util.DateUtil.isSpring;
import static se.sundsvall.citizenchanges.util.NumberFormatter.formatMobileNumber;
import static se.sundsvall.citizenchanges.util.OepErrandQualificationReminderUtil.isOepErrandQualified;
import static se.sundsvall.citizenchanges.util.ValidationUtil.validMSISDN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.api.model.ReportMetaData;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.util.BatchContext;
import se.sundsvall.citizenchanges.util.Constants;
import se.sundsvall.citizenchanges.util.DateUtil;
import se.sundsvall.citizenchanges.util.MessageMapper;

@Service
public class ReminderService {

	private static final Logger LOG = LoggerFactory.getLogger(ReminderService.class);

	private final OpenEIntegration openEIntegration;

	private final MessagingClient messagingClient;

	private final MessageMapper mapper;

	private final ServiceProperties properties;

	public ReminderService(final OpenEIntegration openEIntegration, final MessagingClient messagingClient,
		final MessageMapper mapper, final ServiceProperties properties) {
		this.openEIntegration = openEIntegration;
		this.messagingClient = messagingClient;
		this.mapper = mapper;
		this.properties = properties;
	}

	public BatchStatus runBatch(final int firstErrand, final int numOfErrands, final boolean sendMessage,final String municipalityId) {
		return runBatch(firstErrand, numOfErrands, sendMessage, null, null, null,municipalityId);
	}

	public BatchStatus runBatch(final int firstErrand, final int numOfErrands, final String sms, final String email,final String municipalityId) {
		return runBatch(firstErrand, numOfErrands, true, null, sms, email,municipalityId);
	}

	public BatchStatus runBatch(final int firstErrand, final int numOfErrands, final boolean sendMessage, final List<String> oepErrands, final String sms, final String email,final String municipalityId) {
		LOG.info("Batch job to send reminders is running...");

		final var batchContext = BatchContext.builder()
			.withFirstErrand(firstErrand)
			.withNumberOfErrands(numOfErrands)
			.withSendMessages(sendMessage)
			.withOepErrandIds(oepErrands)
			.withSms(sms)
			.withEmail(email)
			.build();

		for (final var familyId : getFamilyIdArray()) {
			processFamilyId(familyId, batchContext,municipalityId);
		}
		return BatchStatus.DONE;
	}

	private String[] getFamilyIdArray() {
		return properties.familyId().split(",");
	}

	private void processFamilyId(final String familyId, final BatchContext batchContext,final String municipalityId) {
		final var familyType = getFamilyType(familyId);

		if (FamilyType.SKOLSKJUTS.equals(familyType)) {
			final var errandItemList = getQualifiedErrands(familyId, batchContext, municipalityId);
			final var metaData = ReportMetaData.builder()
				.withReportType(familyType.toString())
				.withInspectErrandsCount(errandItemList.size())
				.withOepStartDate(DateUtil.getFromDateOeP(LocalDate.now()).toString())
				.withReportTimestamp(DateUtil.format(LocalDateTime.now())).build();
			composeAndSendReport(metaData, errandItemList, familyType,municipalityId);
		}
	}

	private List<OepErrandItem> getQualifiedErrands(final String familyId, final BatchContext batchContext, final String municipalityId) {
		final var errandItemList = new ArrayList<OepErrandItem>();
		final var oepErrands = Optional.ofNullable(batchContext.getOepErrandIds())
			.orElseGet(() -> getErrandIdsFromOeP(familyId, DateUtil.getFromDateOeP(LocalDate.now()).toString(), LocalDate.now()));

		if (!oepErrands.isEmpty()) {
			var qualifiedItems = 0;
			for (final var flowInstanceId : oepErrands) {
				processErrand(batchContext, errandItemList, flowInstanceId, qualifiedItems, municipalityId);
				qualifiedItems++;
				if (hasReachedMaxErrands(batchContext, errandItemList)) {
					break;
				}
			}
		}
		return errandItemList;
	}

	private List<String> getErrandIdsFromOeP(final String familyId, final String fromDateOeP, final LocalDate today) {
		return getProcessableSkolskjutsStatuses().stream()
			.map(status -> openEIntegration.getErrandIds(familyId, status, fromDateOeP, today.toString()))
			.flatMap(List::stream)
			.distinct()
			.toList();
	}

	private void processErrand(final BatchContext batchContext, final List<OepErrandItem> errandItemList, final String flowInstanceId, final int qualifiedItems, final String municipalityId) {
		try {
			final var item = openEIntegration.getErrand(flowInstanceId, FamilyType.SKOLSKJUTS);
			if (isOepErrandQualified(item, LocalDate.now()) &&
				((qualifiedItems >= batchContext.getFirstErrand()) &&
					((errandItemList.size() <= batchContext.getNumberOfErrands())
						|| (batchContext.getNumberOfErrands() == 0)))) {
				sendEmailIfRequired(batchContext, item, flowInstanceId, municipalityId);
				sendSmsIfRequired(batchContext, item, flowInstanceId,municipalityId);
				errandItemList.add(item);

			}
		} catch (final Exception e) {
			LOG.error("Failed to get errand item from OeP (flowInstanceId {})", flowInstanceId, e);
		}
	}

	private boolean hasReachedMaxErrands(final BatchContext batchContext, final List<OepErrandItem> errandItemList) {
		if ((batchContext.getNumberOfErrands() > 0) && (errandItemList.size() >= batchContext.getNumberOfErrands())) {
			LOG.info("Reached maximum number of errands: {} (as defined by request parameter batchContext.getNumberOfErrands()). Looping of errands is interrupted.", batchContext.getNumberOfErrands());
			return true;
		}
		return false;
	}

	private void sendEmailIfRequired(final BatchContext batchContext, final OepErrandItem item, final String flowInstanceId,final String municipalityId) {
		sendEmail(batchContext.isSendMessages(), batchContext.getEmail(), item, flowInstanceId, municipalityId);
	}

	private void sendSmsIfRequired(final BatchContext batchContext, final OepErrandItem item, final String flowInstanceId, final String municipalityId) {
		if (item.getContactInfo().isContactBySMS()) {
			sendSMS(batchContext.isSendMessages(), batchContext.getSms(), item, flowInstanceId,municipalityId);
		}
	}

	private void sendEmail(final boolean sendMessage, final String email, final OepErrandItem item, final String flowInstanceId, final String municipalityId) {
		final var recipient = Optional.ofNullable(email)
			.orElseGet(() -> item.getContactInfo().getEmailAddress());

		final var reminderEmailSubject = isSpring() ? REMINDER_EMAIL_SUBJECT_SPRING.formatted(getCurrentYear()) : REMINDER_EMAIL_SUBJECT_AUTUMN.formatted(getNextYear());

		if (sendMessage) {
			LOG.info("Sending reminder email to Messaging service for {}", recipient);
			try {
				final var reminderPayloadEmail = mapper.composeReminderContentEmail(item);
				final var emailRequest = mapper.composeEmailRequest(reminderPayloadEmail, recipient, REMINDER_EMAIL_SENDER, reminderEmailSubject);
				final var messageResult = messagingClient.sendEmail(municipalityId,emailRequest);
				LOG.info("Message response: {}", messageResult);
				if ((messageResult != null) && messageResult.getDeliveries().stream().allMatch(d -> SENT.equals(d.getStatus()))) {
					item.setEmailStatus(STATUS_SENT);
				} else {
					item.setEmailStatus(STATUS_NOT_SENT);
				}
			} catch (final Exception e) {
				item.setEmailStatus(STATUS_FAILED);
				LOG.error("Failed to send email to \"{}\" (flowInstanceId {}). {}", recipient, flowInstanceId, e.getLocalizedMessage());
			}
		} else {
			LOG.info("Simulating reminder email to {}", recipient);
			item.setEmailStatus(STATUS_SENT);
		}
	}

	private void sendSMS(final boolean sendMessage, final String sms, final OepErrandItem item, final String flowInstanceId, final String municipalityId) {
		final var mobileNumber = Optional.ofNullable(sms)
			.orElseGet(() -> item.getContactInfo().getPhoneNumber());

		final var formattedMobileNumber = formatMobileNumber(mobileNumber);

		if (validMSISDN(formattedMobileNumber)) {
			final var targetYear = isSpring() ? String.valueOf(LocalDate.now().getYear()) : String.valueOf(LocalDate.now().plusYears(1).getYear());
			if (sendMessage) {
				LOG.info("Sending reminder SMS to Messaging service for {}", formattedMobileNumber);
				try {
					final var reminderPayloadSMS = mapper.composeReminderContentSMS(item, targetYear);
					final var smsRequest = mapper.composeSmsRequest(reminderPayloadSMS, formattedMobileNumber);
					final var messageResponse = messagingClient.sendSms(municipalityId,smsRequest);
					LOG.info("SmsResponse: {}", messageResponse);
					item.setSmsStatus(STATUS_SENT);
				} catch (final Exception e) {
					item.setSmsStatus(STATUS_FAILED);
					LOG.error("Failed to send SMS to \"{}\" (flowInstanceId {}). {}", formattedMobileNumber, flowInstanceId, e.getLocalizedMessage());
				}
			} else {
				LOG.info("Simulating reminder SMS to \"{}\"...", formattedMobileNumber);
				item.setSmsStatus(STATUS_SENT);
			}
		} else {
			LOG.info("SMS not sent to {}, bad format.", formattedMobileNumber);
			item.setSmsStatus(STATUS_NOT_SENT);
		}
	}

	private void composeAndSendReport(final ReportMetaData metaData, final List<OepErrandItem> errandItemList, final FamilyType familyType,final String municipalityId) {
		// Compose and send report
		final var reminderReportEmailSubject = LocalDate.now().getMonthValue() < 7 ? REMINDER_REPORT_EMAIL_SUBJECT_SPRING + LocalDate.now().getYear() : REMINDER_REPORT_EMAIL_SUBJECT_AUTUMN + LocalDate.now().plusYears(1).getYear();
		final var reportSubject = reminderReportEmailSubject + " (" + metaData.getReportTimestamp() + ")";

		final var htmlPayload = Optional.ofNullable(mapper.composeReminderReportHtmlContent(errandItemList, metaData))
			.orElse("");

		Arrays.stream(mapper.getEmailRecipients(familyType)).forEach(recipient -> {
			final var request = mapper.composeEmailRequest(htmlPayload, recipient, EMAIL_SENDER_NAME, reportSubject);
			LOG.info("Sending reminder report to Messaging service for \" {} \" for  {} ...", recipient, familyType);
			final var messageResult = messagingClient.sendEmail(municipalityId,request);
			LOG.info("EmailResponse: {}", messageResult);
		});
	}

	private FamilyType getFamilyType(final String familyId) {
		return Constants.getFamilyType(familyId);
	}
}
