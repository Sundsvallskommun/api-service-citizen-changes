package se.sundsvall.citizenchanges.service;

import static se.sundsvall.citizenchanges.util.DateUtil.getFromDate;
import static se.sundsvall.citizenchanges.util.ValidationUtil.isOepErrandQualifiedForDayCareCheck;
import static se.sundsvall.citizenchanges.util.ValidationUtil.shouldProcessErrand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.api.model.DaycareInvestigationItem;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.ReportMetaData;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.scheduler.FileHandler;
import se.sundsvall.citizenchanges.util.Constants;
import se.sundsvall.citizenchanges.util.DateUtil;
import se.sundsvall.citizenchanges.util.MessageMapper;

@Service
public class DaycareCheckService {

	private static final Logger log = LoggerFactory.getLogger(DaycareCheckService.class);

	private static final String REPORT_EMAIL_SENDER = Constants.EMAIL_SENDER_NAME;

	private static final String REPORT_EMAIL_SUBJECT = Constants.DAYCARE_REPORT_EMAIL_SUBJECT;

	private final OpenEIntegration openEIntegration;

	private final MessagingClient messagingClient;

	private final MessageMapper mapper;

	private final FileHandler fileHandler;

	private final ServiceProperties properties;

	public DaycareCheckService(final OpenEIntegration openEIntegration, final MessagingClient messagingClient, final MessageMapper mapper, final FileHandler fileHandler, final ServiceProperties properties) {
		this.openEIntegration = openEIntegration;
		this.messagingClient = messagingClient;
		this.mapper = mapper;
		this.fileHandler = fileHandler;
		this.properties = properties;
	}

	public BatchStatus runBatch(final int firstErrand, final int numOfErrands, final int backtrackDays, final MultipartFile file) throws IOException {
		handleFileTransferAndParsing(file);
		return runBatch(firstErrand, numOfErrands, null, backtrackDays);
	}

	private void handleFileTransferAndParsing(final MultipartFile file) throws IOException {

		file.transferTo(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
		fileHandler.parse(new File(file.getOriginalFilename()));

	}

	public BatchStatus runBatch(final int firstErrand, final int numOfErrands, final List<String> oepErrands, final int backtrackDays) {
		final var today = LocalDate.now();
		final var istThresholdDate = getFromDate(today, backtrackDays);
		final var startPoint = (backtrackDays == 0) ? Constants.DAYCARE_REPORT_START_POINT : istThresholdDate.toString();
		final var fromDateOeP = DateUtil.getFromDateOeP(today).toString();

		Arrays.stream(properties.familyId().split(","))
			.map(this::getFamilyType)
			.filter(FamilyType.SKOLSKJUTS::equals)
			.forEach(familyType -> {
				final var thisStatus = Constants.OEP_ERRAND_STATUS_DECIDED.toLowerCase();
				final var errandItemList = new ArrayList<DaycareInvestigationItem>();
				final var updatedOepErrands = getErrandsFromOeP(oepErrands, familyType.toString(), thisStatus, fromDateOeP, today);
				processErrands(updatedOepErrands, errandItemList, firstErrand, numOfErrands, today);
				buildReport(familyType, errandItemList, fromDateOeP, startPoint);
			});

		return BatchStatus.DONE;
	}

	private List<String> getErrandsFromOeP(List<String> oepErrands, final String thisFamilyId, final String thisStatus, final String fromDateOeP, final LocalDate today) {
		if (oepErrands == null) {
			oepErrands = openEIntegration.getErrandIds(thisFamilyId, thisStatus, fromDateOeP, today.toString());
		}
		return oepErrands;
	}

	private void buildReport(final FamilyType familyType, final List<DaycareInvestigationItem> errandItemList,
		final String fromDateOeP, final String startPoint) {

		final var metaData = ReportMetaData.builder()
			.withReportType(familyType.toString())
			.withInspectErrandsCount(errandItemList.size())
			.withOepStartDate(fromDateOeP)
			.withReportTimestamp(DateUtil.format(LocalDateTime.now()))
			.withEduCloudStartDate(startPoint)
			.build();

		// Compose and send report
		final var reportSubject = REPORT_EMAIL_SUBJECT + " (" + metaData.getReportTimestamp() + ")";
		final var htmlPayload = mapper.composeDaycareReportHtmlContent(errandItemList, metaData);
		final var emailRecipientsArray = mapper.getEmailRecipients(familyType);

		Arrays.stream(emailRecipientsArray).forEach(thisRecipient -> {
			final var request = mapper.composeEmailRequest(htmlPayload, thisRecipient, REPORT_EMAIL_SENDER, reportSubject);
			log.info("Sending daycare scope report to Messaging service for \" {} \" for {} ...", thisRecipient, familyType);
			final var messageResponse = messagingClient.sendEmail(request);
			log.info("Response: {}", messageResponse);
		});

	}

	private void processErrands(final List<String> oepErrands, final List<DaycareInvestigationItem> errandItemList, final int firstErrand, final int numOfErrands, final LocalDate today) {

		final var qualifiedItems = new AtomicInteger(0);

		oepErrands.stream()
			.map(flowInstanceId -> openEIntegration.getErrand(flowInstanceId, getFamilyType(properties.familyId())))
			.filter(item -> isOepErrandQualifiedForDayCareCheck(item, today))
			.filter(item -> shouldProcessErrand(qualifiedItems.get(), errandItemList.size(), firstErrand, numOfErrands))
			.map(fileHandler::getISTPlacement)
			.filter(Objects::nonNull)
			.limit(numOfErrands)
			.forEach(e -> {
				qualifiedItems.incrementAndGet();
				errandItemList.add(e);
			});

		log.info("Processed {} qualified errands from OeP.", errandItemList.size());
	}

	private FamilyType getFamilyType(final String familyId) {
		return Constants.getFamilyType(familyId);
	}


	public void deleteCachedFile() {

		fileHandler.deleteCachedFile();
	}

	public boolean checkCachedFile() {

		return fileHandler.checkCachedFile();
	}

}
