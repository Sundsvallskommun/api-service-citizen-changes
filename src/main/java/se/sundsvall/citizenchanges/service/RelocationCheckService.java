package se.sundsvall.citizenchanges.service;

import static java.util.Collections.emptyList;
import static se.sundsvall.citizenchanges.util.Constants.EMAIL_SENDER_NAME;
import static se.sundsvall.citizenchanges.util.Constants.META_BACKTRACK_DAYS_DEFAULT;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_READY;
import static se.sundsvall.citizenchanges.util.Constants.getFamilyType;
import static se.sundsvall.citizenchanges.util.Constants.getProcessableSkolskjutsStatuses;
import static se.sundsvall.citizenchanges.util.ValidationUtil.isOepErrandQualifiedForRelocationCheck;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import se.sundsvall.citizenchanges.api.model.ApplicantInfo;
import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.InvestigationItem;
import se.sundsvall.citizenchanges.api.model.ReportMetaData;
import se.sundsvall.citizenchanges.integration.citizen.CitizenIntegration;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.util.DateUtil;
import se.sundsvall.citizenchanges.util.ItemMapper;
import se.sundsvall.citizenchanges.util.MessageConstants;
import se.sundsvall.citizenchanges.util.MessageMapper;

@Service
public class RelocationCheckService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelocationCheckService.class);

	private final CitizenIntegration citizenIntegration;

	private final OpenEIntegration openEIntegration;

	private final MessagingClient messagingClient;

	private final MessageMapper mapper;

	private final ItemMapper itemMapper;

	private final ServiceProperties properties;

	public RelocationCheckService(final CitizenIntegration citizenIntegration, final OpenEIntegration openEIntegration,
		final MessagingClient messagingClient, final MessageMapper mapper, final ItemMapper itemMapper, final ServiceProperties properties) {
		this.citizenIntegration = citizenIntegration;
		this.openEIntegration = openEIntegration;
		this.messagingClient = messagingClient;
		this.mapper = mapper;
		this.itemMapper = itemMapper;
		this.properties = properties;
	}

	public BatchStatus runBatch() {
		// Mocked. Backtrack value should be fetched from DB, based on last successful execution.
		return runBatch(Optional.of(META_BACKTRACK_DAYS_DEFAULT), null);
	}

	public BatchStatus runBatch(final Optional<Integer> backtrackDays, final Set<CitizenWithChangedAddress> citizens) {
		LOGGER.info("Batch job running...");

		final var today = LocalDate.now();
		final var fromDateMeta = DateUtil.getFromDateMeta(today, backtrackDays.orElse(META_BACKTRACK_DAYS_DEFAULT)).toString();
		final var fromDateOeP = DateUtil.getFromDateOeP(today).toString();
		// Get information from Metakatalogen about registered moves
		final var moves = getMoves(fromDateMeta, citizens);

		if (moves.isEmpty()) {
			return BatchStatus.ERROR;
		}

		// Get information from OeP for defined familyId values
		for (final String thisFamilyId : properties.familyId().split(",")) {
			final var familyType = getFamilyType(thisFamilyId);
			final var statuses = getStatusesToProcess(familyType);

			// Get errands list from OeP and check if there are errands qualified for investigation
			final var oepErrands = getErrandsFromOeP(statuses, thisFamilyId, fromDateOeP, today);
			LOGGER.info("Found a total of {} errands with correct status", oepErrands.size());

			List<InvestigationItem> investigationItemList = new ArrayList<>();
			if (!oepErrands.isEmpty()) {
				for (final String flowInstanceId : oepErrands) {
					investigationItemList.addAll(mapFlowInstance(flowInstanceId, familyType, today, moves));
				}
			} else {
				LOGGER.info("No errands could be retrieved from OeP for {} ({}).", familyType, thisFamilyId);
			}

			// Remove duplicates from list and sort it properly
			investigationItemList = cleanAndSort(investigationItemList);
			composeEmail(familyType, investigationItemList, moves, fromDateOeP, fromDateMeta);

		}
		return BatchStatus.DONE;
	}

	private List<String> getStatusesToProcess(FamilyType familyType) {
		if (familyType.equals(FamilyType.ELEVRESA)) {
			return List.of(OEP_ERRAND_STATUS_READY.toLowerCase());
		}
		return getProcessableSkolskjutsStatuses();
	}

	private List<String> getErrandsFromOeP(final List<String> statuses, final String familyId, final String fromDateOeP, final LocalDate today) {
		return statuses.stream()
			.map(status -> openEIntegration.getErrandIds(familyId, status, fromDateOeP, today.toString()))
			.flatMap(List::stream)
			.distinct()
			.toList();
	}

	private List<InvestigationItem> mapFlowInstance(final String flowInstanceId, final FamilyType familyType, final LocalDate today, final Map<String, CitizenWithChangedAddress> moves) {
		final var tempList = new ArrayList<InvestigationItem>();
		try {
			final var item = openEIntegration.getErrand(flowInstanceId, familyType);

			if (isOepErrandQualifiedForRelocationCheck(item, today)) {

				final var minorIdentifier = item.getMinorIdentifier();
				final var applicants = item.getApplicants();
				final var applicantMoves = new ArrayList<CitizenWithChangedAddress>();
				// Iterate all applicants to find matching address changes

				Optional.ofNullable(applicants)
					.orElse(emptyList()).stream()
					.map(ApplicantInfo::getApplicantIdentifier)
					.map(moves::get)
					.filter(Objects::nonNull)
					.forEach(parentMove -> {
						applicantMoves.add(parentMove);
						final boolean sameAddress = Optional.ofNullable(minorIdentifier)
							.map(moves::get)
							.map(minorM -> gotSameAddress(parentMove, minorM))
							.orElse(false);
						tempList.add(itemMapper.composeInvestigationItem(parentMove, item, familyType, parentMove.getPersonNumber(), sameAddress));
					});

				Optional.ofNullable(minorIdentifier)
					.map(moves::get)
					.ifPresent(thisMove -> {
						final var sameAddress = applicantMoves.stream()
							.anyMatch(applicantMove -> gotSameAddress(thisMove, applicantMove));
						if (sameAddress) {
							LOGGER.info("Errand {} : The minor has moved to the same address as a guardian. Skip separate investigation item.", item.getFlowInstanceId());
						} else {
							tempList.add(itemMapper.composeInvestigationItem(thisMove, item, familyType, minorIdentifier, false));
						}
					});
			}
			LOGGER.info("tempList size: {}", tempList.size());
		} catch (final Exception e) {
			LOGGER.error("Failed to get errand item from OeP (flowInstanceId " + flowInstanceId + ")", e);
		}
		return tempList;
	}

	private void composeEmail(final FamilyType familyType, final List<InvestigationItem> investigationItemList,
		final Map<String, CitizenWithChangedAddress> moves, final String fromDateOeP, final String fromDateMeta) {

		final var metaData = ReportMetaData.builder()
			.withReportType(familyType.toString())
			.withChangedAddressesCount(moves.size())
			.withInspectErrandsCount(investigationItemList.size())
			.withMetaStartDate(fromDateMeta)
			.withOepStartDate(fromDateOeP)
			.withReportTimestamp(DateUtil.format(LocalDateTime.now()))
			.build();

		final var htmlPayload = mapper.composeHtmlContent(familyType, investigationItemList, metaData);
		final var emailRecipientsArray = mapper.getEmailRecipients(familyType);
		final var subject = MessageConstants.valueOf("EMAIL_SUBJECT_" + familyType.toString().toUpperCase()).getText();

		for (final String thisRecipient : emailRecipientsArray) {
			final var request = mapper.composeEmailRequest(htmlPayload, thisRecipient, EMAIL_SENDER_NAME, subject);
			LOGGER.info("Sending message request to Messaging service for \"{}\" for {} report... ", thisRecipient, familyType);
			final var messageResponse = messagingClient.sendEmail(request);
			LOGGER.info("Response: {}", messageResponse);
		}
	}

	private List<InvestigationItem> cleanAndSort(final List<InvestigationItem> thisList) {
		final var compareByName = Comparator
			.comparing(InvestigationItem::getSchoolUnit)
			.thenComparing(InvestigationItem::getFlowInstanceId);

		return thisList.stream()
			.distinct()
			.sorted(compareByName)
			.toList();
	}

	private boolean gotSameAddress(final CitizenWithChangedAddress parentMove, final CitizenWithChangedAddress minorMove) {
		// Determines if the addresses (filtered by status - "Current" or "Previous") are equal for both moves, based on
		// coordinates for the address.
		// If an address or coordinates are missing, false is returned.
		final var parentAddress = parentMove.getAddresses().stream().filter(move -> move.getStatus().equals("Current")).findAny().orElse(null);
		final var minorAddress = minorMove.getAddresses().stream().filter(move -> move.getStatus().equals("Current")).findAny().orElse(null);
		if (parentAddress != null && minorAddress != null) {
			return (parentAddress.getxCoordLocal().equals(minorAddress.getyCoordLocal()) && parentAddress.getyCoordLocal().equals(minorAddress.getyCoordLocal()));
		}
		return false;
	}

	private Map<String, CitizenWithChangedAddress> getMoves(final String fromDate, final Set<CitizenWithChangedAddress> citizens) {
		LOGGER.info("Retrieving changed addresses from Metakatalogen (changedDateFrom: {})...", fromDate);
		final Set<CitizenWithChangedAddress> moves;
		if (citizens != null && !citizens.isEmpty()) {
			moves = citizens;
		} else {
			moves = citizenIntegration.getAddressChanges(fromDate);
		}
		LOGGER.info("Got {} changed addresses from Metakatalogen.", moves.size());
		return moves.stream().collect(Collectors.toMap(CitizenWithChangedAddress::getPersonNumber, Function.identity()));
	}

}
