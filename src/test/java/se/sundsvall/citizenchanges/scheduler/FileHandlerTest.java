package se.sundsvall.citizenchanges.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileHandlerTest {

	@Mock
	private List<ParsedRow> list;

	@InjectMocks
	private FileHandler fileHandler;

	@ParameterizedTest
	@ValueSource(strings = {"Nej", "Ja, deltid"})
	void getISTPlacement_nej_ja_deltid(final String daycarePlacement) {
		// Arrange
		final var parsedRows = new ArrayList<ParsedRow>();
		parsedRows.add(ParsedRow.builder()
			.withPersonId("12")
			.withUnit("someUnit")
			.withPlacementStart(LocalDate.now().minusMonths(1).toString())
			.withChangeStart(LocalDate.now().toString())
			.withPlacementEnd(LocalDate.now().plusMonths(1).toString())
			.withTaxCategory("Fritidshem heltid")
			.withGuardian1("someApplicantIdentifier\nTest Testorsson")
			.withGuardian2("someApplicantIdentifier2\nTest Testorsson")
			.build());

		final var item = buildOepErrandItem(daycarePlacement, "someApplicantIdentifier", "12");

		// Mock
		when(list.stream()).thenReturn(parsedRows.stream());

		// Act
		final var result = fileHandler.getISTPlacement(item);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getIstPlacement()).isEqualTo("Fritidshem heltid");
		assertThat(result.getIstPlacementName()).isEqualTo("someUnit");
		assertThat(result.getIstChangeStartDate()).isEqualTo(LocalDate.now().toString());
		assertThat(result.getIstPlacementEndDate()).isEqualTo(LocalDate.now().plusMonths(1).toString());
		assertThat(result.getFlowInstanceId()).isEqualTo("someFlowInstanceId");
		assertThat(result.getDecisionStart()).isEqualTo(LocalDate.now().minusYears(1).toString());
		assertThat(result.getDecisionEnd()).isEqualTo(LocalDate.now().plusMonths(3).toString());
		assertThat(result.getFamilyId()).isEqualTo("someFamilyId");
		assertThat(result.getAdministratorName()).isEqualTo("someAdministratorName");
		assertThat(result.getSchoolUnit()).isEqualTo("someSchoolUnit");
		assertThat(result.getDaycarePlacement()).isEqualTo(daycarePlacement);
	}


	@Test
	void getISTPlacementWithDayCare_Heltid() {

		// Arrange
		final var parsedRows = new ArrayList<ParsedRow>();
		parsedRows.add(ParsedRow.builder()
			.withPersonId("12")
			.withUnit("someUnit")
			.withPlacementStart(LocalDate.now().minusMonths(1).toString())
			.withChangeStart(LocalDate.now().toString())
			.withPlacementEnd(LocalDate.now().plusMonths(1).toString())
			.withTaxCategory("Fritidshem heltid")
			.withGuardian1("someApplicantIdentifier\nTest Testorsson")
			.withGuardian2("someApplicantIdentifier2\nTest Testorsson")
			.build());

		final var item = buildOepErrandItem("Ja, heltid", "someApplicantIdentifier", "12");

		// Mock
		when(list.stream()).thenReturn(parsedRows.stream());

		// Act
		final var result = fileHandler.getISTPlacement(item);

		// Assert
		assertThat(result).isNull();

	}

	@Test
	void parse() throws IOException {
		// Arrange
		final var lastRunPath = Path.of("lastRun.xls");
		final var file = new File("src/test/resources/mockfiles/mockfile.xls");

		if (file.exists()) {
			try (final var inputStream = new FileInputStream(file)) {
				Files.copy(inputStream, lastRunPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Act
		fileHandler.parse(file);

		// Assert
		assertThat(fileHandler.getParsedRows()).isNotEmpty();
	}

	@AfterEach
	void cleanup() throws IOException {
		final var lastRunPath = Path.of("lastRun.xls");
		final var mockFilePath = "src/test/resources/mockfiles/mockfile.xls";
		if (new File(mockFilePath).createNewFile() && Files.exists(lastRunPath)) {
			try (final var inputStream = new FileInputStream(lastRunPath.toAbsolutePath().toString())) {
				Files.copy(inputStream, Path.of(mockFilePath), StandardCopyOption.REPLACE_EXISTING);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}

			Files.delete(lastRunPath);
		}

	}

}
