package se.sundsvall.citizenchanges.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.citizenchanges.api.model.ApplicantInfo;
import se.sundsvall.citizenchanges.api.model.DaycareInvestigationItem;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.dept44.util.jacoco.ExcludeFromJacocoGeneratedCoverageReport;

@Component
@ExcludeFromJacocoGeneratedCoverageReport
public class FileHandler {

	private static final Logger log = LoggerFactory.getLogger(FileHandler.class);

	private static final String BACKUP_FILE_PATH = "lastRun.xls";

	private final PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
		.ignoreWhitespaces(true)
		.addListDelimiter(";")
		.build();


	private List<ParsedRow> parsedRows;

	// Used for testing purposes
	List<ParsedRow> getParsedRows() {
		return parsedRows;
	}

	public DaycareInvestigationItem getISTPlacement(final OepErrandItem item) {
		return findRow(item.getMinorIdentifier(), item.getDaycarePlacement(), item)
			.map(result -> {
				log.info("Row found for flowInstance: {}", item.getFlowInstanceId());
				return composeInvestigationItem(item, DaycareInvestigationItem.builder()
					.withIstChangeStartDate(result.getChangeStart())
					.withIstPlacement(result.getTaxCategory())
					.withIstPlacementEndDate(result.getPlacementEnd())
					.withIstPlacementName(result.getUnit())
					.build());
			})
			.orElse(null);
	}

	private Optional<ParsedRow> findRow(final String minorIdentifier, final String daycarePlacement, final OepErrandItem item) {
		return parsedRows.stream()
			.filter(parsedRow -> checkIfPlacementIsActive(parsedRow.getPlacementEnd()))
			.filter(parsedRow -> checkIfPlacementIsWithinRangeOfDecision(parsedRow, item))
			.filter(parsedRow -> checkIfApplicantIsTheSameAsPlacement(parsedRow, item))
			.filter(parsedRow -> parsedRow.getPersonId().replace("-", "").equals(minorIdentifier))
			.filter(parsedRow -> checkForIncrease(parsedRow.getTaxCategory(), daycarePlacement))
			.findFirst();
	}

	private boolean checkIfPlacementIsWithinRangeOfDecision(final ParsedRow parsedRow, final OepErrandItem item) {
		final var changeDate = LocalDate.parse(parsedRow.getChangeStart());
		return changeDate.isAfter(LocalDate.parse(item.getDecisionStart())) && changeDate.isBefore(LocalDate.parse(item.getDecisionEnd()));
	}

	private boolean checkIfPlacementIsActive(final String placementStop) {
		if (placementStop.isEmpty()) {
			return true;
		}
		return LocalDate.parse(placementStop).isAfter(LocalDate.now());
	}

	private boolean checkIfApplicantIsTheSameAsPlacement(final ParsedRow row, final OepErrandItem item) {
		if (!row.getGuardian2().isEmpty()) {
			return true;
		}

		return item.getApplicants().stream()
			.filter(ApplicantInfo::isPrimaryGuardian)
			.findFirst()
			.orElse(ApplicantInfo.builder().build())
			.getApplicantIdentifier()
			.equals(row.getCalculatingFounder().split("\n")[0]);

	}

	private boolean checkForIncrease(final String actualPlacement, final String applicationPlacement) {
		return switch (applicationPlacement) {
			case "Nej" ->
				"Fritidshem heltid".equals(actualPlacement) || "Fritidshem deltid".equals(actualPlacement);
			case "Ja, deltid" -> "Fritidshem heltid".equals(actualPlacement);
			default -> false;
		};
	}

	public void parse(final File newFile) throws IOException {

		final var oldFile = new File(BACKUP_FILE_PATH);
		parsedRows = Poiji.fromExcel(newFile, ParsedRow.class, options);

		if (oldFile.exists()) {
			final var lastRunsRows = Poiji.fromExcel(oldFile, ParsedRow.class, options).stream()
				.map(ParsedRow::toString)
				.collect(Collectors.toSet());

			parsedRows = parsedRows.stream()
				.filter(e -> !lastRunsRows.contains(e.toString()))
				.toList();
		}
		log.info("Totalt parsed rows: {}", parsedRows.size());
		storeFileForNextRun(newFile);
	}

	private void storeFileForNextRun(final File newFile) throws IOException {

		try (final var inputStream = new FileInputStream(newFile)) {
			Files.copy(inputStream, Path.of(BACKUP_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
		}

		Files.delete(newFile.toPath());
	}

	private DaycareInvestigationItem composeInvestigationItem(final OepErrandItem item,
		final DaycareInvestigationItem daycareDetails) {
		return DaycareInvestigationItem.builder()
			.withFlowInstanceId(item.getFlowInstanceId())
			.withFamilyId(item.getFamilyId())
			.withDecisionStart(item.getDecisionStart())
			.withDecisionEnd(item.getDecisionEnd())
			.withSchool(item.getSchool())
			.withSchoolUnit(item.getSchoolUnit())
			.withAdministratorName(item.getAdministratorName())
			.withDaycarePlacement(item.getDaycarePlacement())
			.withIstChangeStartDate(daycareDetails.getIstChangeStartDate())
			.withIstPlacementEndDate(daycareDetails.getIstPlacementEndDate())
			.withIstPlacementName(daycareDetails.getIstPlacementName())
			.withIstPlacement(daycareDetails.getIstPlacement())
			.build();
	}

	public void deleteCachedFile() {
		try {
			Files.delete(Path.of(BACKUP_FILE_PATH));
		} catch (final IOException e) {
			throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, "Could not delete cached file");
		}
	}

	public boolean checkCachedFile() {
		return new File(BACKUP_FILE_PATH).exists();
	}

}
