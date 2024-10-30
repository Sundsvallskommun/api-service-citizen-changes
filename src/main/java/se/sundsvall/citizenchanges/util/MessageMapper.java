package se.sundsvall.citizenchanges.util;

import static se.sundsvall.citizenchanges.util.Constants.APPLICANT_CAPACITY_GUARDIAN;
import static se.sundsvall.citizenchanges.util.Constants.DAYCARE_REPORT_LEAD_TEXT;
import static se.sundsvall.citizenchanges.util.Constants.EMAIL_SENDER_ADDRESS;
import static se.sundsvall.citizenchanges.util.Constants.GUARDIAN_NOTATION;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_DAYCARE_INTRO;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_DAYCARE_TABLE_HEAD;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_DAYCARE_TABLE_ROW;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_IMG_CUSTOM;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_IMG_DEFAULT;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_INTRO;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_LINK;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_MAIN_END;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_MAIN_START;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_NONBREAKINGHYPHEN;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_REMINDER_REPORT_INTRO;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_REMINDER_REPORT_TABLE_HEAD;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_REMINDER_REPORT_TABLE_ROW;
import static se.sundsvall.citizenchanges.util.Constants.HTML_TEMPLATE_REMINDER_TEXT_BODY;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_LAST_DAY_AUTUMN;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_LAST_DAY_SPRING;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_SMS_BODY;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_SMS_SENDER;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_TARGET_SEMESTER_AUTUMN;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_TARGET_SEMESTER_SPRING;
import static se.sundsvall.citizenchanges.util.Constants.STATUS_NO;
import static se.sundsvall.citizenchanges.util.Constants.STATUS_YES;
import static se.sundsvall.citizenchanges.util.Constants.TRUSTEE_NOTATION;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import se.sundsvall.citizenchanges.api.model.AddressItem;
import se.sundsvall.citizenchanges.api.model.DaycareInvestigationItem;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.InvestigationItem;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.api.model.ReportMetaData;

import generated.se.sundsvall.messaging.EmailRequest;
import generated.se.sundsvall.messaging.EmailSender;
import generated.se.sundsvall.messaging.SmsRequest;

@Component
@EnableConfigurationProperties(MessageMapperProperties.class)
public class MessageMapper {

	private static final String LINK = "%%link%%";

	private static final String ROW_CONTENT = "%%rowContent%%";

	private static final String REPORT_TIMESTAMP = "%%reportTimestamp%%";

	private static final String LOGO_IMAGE = "%%logoImage%%";

	private static final String INSPECT_ERRANDS_COUNT = "%%inspectErrandsCount%%";

	private static final String EXTRA_VALUE = "%%extraValue%%";

	private static final String DECISION_END = "%%decisionEnd%%";

	private static final String NB_HYPHEN = HTML_TEMPLATE_NONBREAKINGHYPHEN;

	private final Logger log = LoggerFactory.getLogger(MessageMapper.class);

	private final MessageMapperProperties properties;

	public MessageMapper(final MessageMapperProperties properties) {
		this.properties = properties;
	}

	public EmailRequest composeEmailRequest(final String payload, final String emailRecipient, final String senderName, final String subject) {

		return new EmailRequest()
			.sender(new EmailSender()
				.name(senderName)
				.address(EMAIL_SENDER_ADDRESS))
			.emailAddress(emailRecipient)
			.subject(subject)
			.htmlMessage(Base64.getEncoder().encodeToString(payload.getBytes()));
	}

	public SmsRequest composeSmsRequest(final String payload, final String mobileNumber) {
		return new SmsRequest()
			.sender(REMINDER_SMS_SENDER)
			.mobileNumber(mobileNumber)
			.message(payload);
	}

	public String getImageData(final String fileName) throws IOException {
		final byte[] fileContent;
		try (final InputStream is = getClass().getResourceAsStream(fileName)) {
			fileContent = Objects.requireNonNull(is).readAllBytes();
		}
		return Base64.getEncoder().encodeToString(fileContent);
	}

	public String[] getEmailRecipients(final FamilyType familyType) {
		if (FamilyType.SKOLSKJUTS.equals(familyType)) {
			return properties.recipientsSkolskjuts().split(",");
		}
		return properties.recipientsElevresa().split(",");
	}

	public String composeHtmlContent(final FamilyType familyType, final List<InvestigationItem> items, final ReportMetaData metaData) {
		String mainStart = Constants.HTML_TEMPLATE_MAIN_START;
		String listingHeader = Constants.HTML_TEMPLATE_TABLE_HEAD;
		String listingRow = Constants.HTML_TEMPLATE_TABLE_ROW;
		final String extraHeaderColumn = Constants.HTML_TEMPLATE_EXTRA_HEADERCOL;
		final String extraTableRowColumn = Constants.HTML_TEMPLATE_EXTRA_ROWCOL;
		final StringBuilder payload = new StringBuilder();
		final String emptyString = "";
		final boolean isSkolskjuts = FamilyType.SKOLSKJUTS.equals(familyType);

		final String imgTag = getImgTag();

		final StringBuilder rows = new StringBuilder(emptyString);
		String previousRow = emptyString;
		int rowCount = 0;
		// Iterate all items of the investigation items and compose line content
		if (!items.isEmpty()) {
			for (final InvestigationItem thisItem : items) {

				final String tempSchool;
				String extraColumns = "";
				if (isSkolskjuts) {
					final String[] names = {
						thisItem.getNameGuardian(), thisItem.getNameMinor()
					};
					final String joinedNames = Stream.of(names).filter(s -> (s != null) && !s.isEmpty()).collect(Collectors.joining("<br>")); // join with linebreaks, but only for tokens having content
					final String[] types = thisItem.getMoverList().split(",");
					final String joinedTypes = Stream.of(types).filter(s -> (s != null) && !s.isEmpty()).collect(Collectors.joining(" + "));
					extraColumns = extraTableRowColumn.replace(EXTRA_VALUE, joinedNames.trim()) +
						extraTableRowColumn.replace(EXTRA_VALUE, joinedTypes.trim()) +
						extraTableRowColumn.replace(EXTRA_VALUE, thisItem.getMinorToSameAddress() +
							extraTableRowColumn.replace(EXTRA_VALUE, Optional.ofNullable(thisItem.getAdministratorName()).orElse("")));

					tempSchool = Optional.ofNullable(thisItem.getSchoolUnit()).orElse("")
						+ " "
						+ Optional.ofNullable(thisItem.getSchoolProgram()).orElse("");
				} else {
					listingHeader = Constants.HTML_TEMPLATE_TABLE_HEAD_STUDENT_TRAVEL;
					listingRow = Constants.HTML_TEMPLATE_TABLE_ROW_STUDENT_TRAVEL;

					tempSchool = Optional.ofNullable(thisItem.getSchoolUnit()).orElse("");

					listingRow = listingRow.replace("%%program%%",
						Optional.ofNullable(thisItem.getSchoolProgram())
							.filter(str -> !str.isEmpty())
							.orElse("Uppgift saknas"));

				}

				final var newAddress = thisItem.getNewAddress();
				final var oldAddress = thisItem.getOldAddress();

				final var newAddressStr = parseAddressString(newAddress);
				final var oldAddressStr = parseAddressString(oldAddress);

				final var thisLink = Constants.HTML_TEMPLATE_LINK.replace(LINK, properties.linkTemplate()).replace("%%flowinstanceid" +
					"%%",
					Optional.ofNullable(thisItem.getFlowInstanceId()).orElse(""));

				final String tempRow = listingRow.replace(LINK, thisLink)
					.replace("%%schoolUnit%%", tempSchool)
					.replace("%%decisionStart%%", Optional.ofNullable(thisItem.getDecisionStart()).orElse("").replace("-", NB_HYPHEN))
					.replace(DECISION_END, Optional.ofNullable(thisItem.getDecisionEnd()).orElse("").replace("-", NB_HYPHEN))
					.replace("%%moveDate%%", Optional.ofNullable(newAddress.getNrDate())
						.map(date -> date.toLocalDate().toString())
						.orElse(""))
					.replace("%%newAddress%%", newAddressStr.replaceAll("\\s+", " "))
					.replace("%%oldAddress%%", oldAddressStr.replaceAll("\\s+", " "))
					.replace("%%status%%", Optional.ofNullable(thisItem.getStatus()).orElse(""))
					.replace("%%classified%%", Optional.ofNullable(thisItem.getClassified()).orElse(""))
					.replace("%%extraColumns%%", extraColumns);
				if (!previousRow.equals(tempRow)) {
					// include only if the row is different from the previous row.
					rows.append(tempRow);
					rowCount = rowCount + 1;
				}

				previousRow = tempRow;

				log.info("FlowId: {}, SchoolUnit: {}, ListingRow: {}", thisItem.getFlowInstanceId(),
					thisItem.getSchoolUnit(), listingRow);
			}
		}

		mainStart = mainStart.concat(HTML_TEMPLATE_INTRO).replace("%%familyType%%", familyType.toString().toLowerCase())
			.replace(REPORT_TIMESTAMP, metaData.getReportTimestamp())
			.replace("%%metaStartDate%%", metaData.getMetaStartDate())
			.replace("%%oepStartDate%%", metaData.getOepStartDate())
			.replace("%%changedAddressesCount%%", String.valueOf(metaData.getChangedAddressesCount()))
			.replace(INSPECT_ERRANDS_COUNT, String.valueOf(rowCount))
			.replace(LOGO_IMAGE, imgTag);
		payload.append(mainStart);

		var tempHeader = listingHeader.replace("%%headline%%", MessageConstants.valueOf(
			"HEADLINE_" + familyType.toString().toUpperCase()).getText())
			.replace("%%leadText%%", MessageConstants.valueOf("LEAD_TEXT_" + familyType.toString().toUpperCase()).getText());

		var extraHeaders = "";

		if (isSkolskjuts) {
			extraHeaders = extraHeaderColumn.replace(EXTRA_VALUE, "Namn&nbsp;(i ansökan)") +
				extraHeaderColumn.replace(EXTRA_VALUE, "Flytt&nbsp;av") +
				extraHeaderColumn.replace(EXTRA_VALUE, "VH och barn flyttat till samma adress") +
				extraHeaderColumn.replace(EXTRA_VALUE, "Handläggare");
		}
		tempHeader = tempHeader.replace("%%extraHeaders%%", extraHeaders);

		payload.append(tempHeader.replace(ROW_CONTENT, rows));
		payload.append(HTML_TEMPLATE_MAIN_END);

		return payload.toString();
	}

	private String parseAddressString(final AddressItem addressItem) {
		return String.join("",
			Optional.ofNullable(addressItem.getAddress()).map(s -> s + " ").orElse(""),
			Optional.ofNullable(addressItem.getApartmentNumber()).map(s -> s + "<br>").orElse(""),
			Optional.ofNullable(addressItem.getCo()).filter(s -> !s.trim().isEmpty()).map(s -> s + "<br>").orElse(""),
			Optional.ofNullable(addressItem.getPostalCode()).map(s -> s + " ").orElse(""),
			Optional.ofNullable(addressItem.getCity()).orElse(""))
			.replaceAll("\\s", "&nbsp;")
			.trim();
	}

	public String composeDaycareReportHtmlContent(final List<DaycareInvestigationItem> items, final ReportMetaData metaData) {

		final var rows = items.stream().map(this::getString).collect(Collectors.joining());

		final var mainStart = HTML_TEMPLATE_MAIN_START.concat(HTML_TEMPLATE_DAYCARE_INTRO)
			.replace("%%familyType%%", metaData.getReportType().toLowerCase())
			.replace(REPORT_TIMESTAMP, metaData.getReportTimestamp())
			.replace("%%startDate%%", metaData.getEduCloudStartDate())
			.replace("%%oepStartDate%%", metaData.getOepStartDate())
			.replace(INSPECT_ERRANDS_COUNT, String.valueOf(items.size()))
			.replace(LOGO_IMAGE, getImgTag());

		final var payload = new StringBuilder();
		payload.append(mainStart);

		final var tempHeader = HTML_TEMPLATE_DAYCARE_TABLE_HEAD.replace("%%headline%%", metaData.getReportType())
			.replace("%%leadText%%", DAYCARE_REPORT_LEAD_TEXT);

		payload.append(tempHeader.replace(ROW_CONTENT, rows));
		payload.append(HTML_TEMPLATE_MAIN_END);

		return payload.toString();
	}

	@NotNull
	private String getString(final DaycareInvestigationItem thisItem) {
		final String thisLink = HTML_TEMPLATE_LINK.replace(LINK, properties.linkTemplate())
			.replace("%%flowinstanceid%%", thisItem.getFlowInstanceId());

		return HTML_TEMPLATE_DAYCARE_TABLE_ROW.replace(LINK, thisLink)
			.replace("%%schoolUnit%%", (thisItem.getSchool() + " " + thisItem.getSchoolUnit()))
			.replace("%%decisionStart%%", Optional.ofNullable(thisItem.getDecisionStart()).orElse("").replace("-", NB_HYPHEN))
			.replace(DECISION_END, Optional.ofNullable(thisItem.getDecisionEnd()).orElse("").replace("-", NB_HYPHEN))
			.replace("%%oldScope%%", Optional.ofNullable(thisItem.getDaycarePlacement()).orElse(""))
			.replace("%%newScope%%", Optional.ofNullable(thisItem.getIstPlacement()).orElse(""))
			.replace("%%placementStart%%", Optional.ofNullable(thisItem.getIstChangeStartDate()).orElse("").replace("-", NB_HYPHEN))
			.replace("%%daycareUnit%%", Optional.ofNullable(thisItem.getIstPlacementName()).orElse(""))
			.replace("%%administrator%%", Optional.ofNullable(thisItem.getAdministratorName()).orElse(""));
	}

	private String getImgTag() {
		var imgTag = HTML_TEMPLATE_IMG_DEFAULT;
		try {
			final var imageData = getImageData("/images/logo.png");
			imgTag = HTML_TEMPLATE_IMG_CUSTOM.replace("%%imageData%%", imageData);
		} catch (final Exception e) {
			log.error("Failed to load image for email header. Sticking with default image. \n\r{} \n\r{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
		}
		return imgTag;
	}

	public String composeReminderReportHtmlContent(final List<OepErrandItem> items, final ReportMetaData metaData) {

		final var payload = new StringBuilder();

		final var rows = items.stream().map(this::getRow)
			.collect(Collectors.joining());

		// Iterate all qualified errands and compose line content

		final var mainStart = HTML_TEMPLATE_MAIN_START.concat(HTML_TEMPLATE_REMINDER_REPORT_INTRO).replace(REPORT_TIMESTAMP, metaData.getReportTimestamp())
			.replace(INSPECT_ERRANDS_COUNT, String.valueOf(items.size()))
			.replace(LOGO_IMAGE, getImgTag());
		payload.append(mainStart);

		payload.append(HTML_TEMPLATE_REMINDER_REPORT_TABLE_HEAD.replace(ROW_CONTENT, rows));
		payload.append(HTML_TEMPLATE_MAIN_END);

		return payload.toString();
	}

	@NotNull
	private String getRow(final OepErrandItem thisItem) {
		final var thisLink = HTML_TEMPLATE_LINK.replace(LINK, properties.linkTemplate()).replace("%%flowinstanceid%%",
			thisItem.getFlowInstanceId());
		final var smsAllowed = thisItem.getContactInfo().isContactBySMS() ? STATUS_YES : STATUS_NO;
		final var role = APPLICANT_CAPACITY_GUARDIAN.equalsIgnoreCase(Optional.ofNullable(thisItem.getApplicantCapacity()).orElse("")) ? GUARDIAN_NOTATION : TRUSTEE_NOTATION;

		return HTML_TEMPLATE_REMINDER_REPORT_TABLE_ROW.replace(LINK, thisLink)
			.replace(DECISION_END,
				Optional.ofNullable(thisItem.getDecisionEnd()).orElse("").replace("-",
					NB_HYPHEN))
			.replace("%%nameMinor%%", Optional.ofNullable(thisItem.getMinorName()).orElse(""))
			.replace("%%contactName%%", Optional.ofNullable(thisItem.getContactInfo().getDisplayName()).orElse("") + " (" + role + ")")
			.replace("%%emailAddress%%", Optional.ofNullable(thisItem.getContactInfo().getEmailAddress()).orElse(""))
			.replace("%%mobileNumber%%", Optional.ofNullable(thisItem.getContactInfo().getPhoneNumber()).orElse(""))
			.replace("%%smsAllowed%%", smsAllowed)
			.replace("%%statusEmail%%", Optional.ofNullable(thisItem.getEmailStatus()).orElse(""))
			.replace("%%statusSMS%%", Optional.ofNullable(thisItem.getSmsStatus()).orElse(""));
	}

	public String composeReminderContentEmail(final OepErrandItem item) {

		final var lastDay = LocalDate.now().getMonthValue() < 7 ? REMINDER_LAST_DAY_SPRING : REMINDER_LAST_DAY_AUTUMN;
		final var targetSemester = LocalDate.now().getMonthValue() < 7 ? REMINDER_TARGET_SEMESTER_SPRING : REMINDER_TARGET_SEMESTER_AUTUMN;
		var mainStart = HTML_TEMPLATE_MAIN_START;

		mainStart = mainStart.concat(HTML_TEMPLATE_REMINDER_TEXT_BODY).replace("%%minorName%%", item.getMinorName())
			.replace("%%errandNumber%%", item.getFlowInstanceId())
			.replace("%%decisionEndDate%%", item.getDecisionEnd())
			.replace(LOGO_IMAGE, getImgTag())
			.replace("%%lastDay%%", lastDay)
			.replace("%%semester%%", targetSemester);

		return mainStart +
			HTML_TEMPLATE_MAIN_END;
	}

	public String composeReminderContentSMS(final OepErrandItem item, final String targetYear) {

		final String lastDay = LocalDate.now().getMonthValue() < 7 ? REMINDER_LAST_DAY_SPRING : REMINDER_LAST_DAY_AUTUMN;
		final var targetSemester = LocalDate.now().getMonthValue() < 7 ? REMINDER_TARGET_SEMESTER_SPRING : REMINDER_TARGET_SEMESTER_AUTUMN;

		var payload = REMINDER_SMS_BODY;
		payload = payload.replace("%%thisYear%%", targetYear)
			.replace("%%decisionEndDate%%", item.getDecisionEnd())
			.replace("%%lastDay%%", lastDay)
			.replace("%%semester%%", targetSemester);

		return payload;
	}

}
