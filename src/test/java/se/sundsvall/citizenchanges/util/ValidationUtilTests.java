package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_SKOLSKJUTS_DENIED;
import static se.sundsvall.citizenchanges.util.ValidationUtil.isOepErrandQualifiedForDayCareCheck;
import static se.sundsvall.citizenchanges.util.ValidationUtil.validMSISDN;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValidationUtilTests {

	@ParameterizedTest
	@ValueSource(strings = {
		"+46701740605", "+4513245"
	})
	void validMSISDNTest(final String number) {
		// Act & Assert
		assertThat(validMSISDN(number)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"0761234567", "070-123 45 67", ""
	})
	void invalidMSISDNTest(final String number) {
		// Act & Assert
		assertThat(validMSISDN(number)).isFalse();
	}

	@Test
	void nullMSISDNTest() {
		// Act & Assert
		assertThat(validMSISDN(null)).isFalse();
	}

	@Test
	void shouldProcessErrand() {
		// Arrange
		final var qualifiedItems = 1;
		final var errandListSize = 1;
		final var firstErrand = 1;
		final var numOfErrands = 1;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isTrue();
	}

	@Test
	void shouldNotProcessErrand() {
		// Arrange
		final var qualifiedItems = 1;
		final var errandListSize = 1;
		final var firstErrand = 2;
		final var numOfErrands = 1;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isFalse();
	}

	@Test
	void shouldProcessErrandNumOfErrandsZero() {
		// Arrange
		final var qualifiedItems = 1;
		final var errandListSize = 1;
		final var firstErrand = 1;
		final var numOfErrands = 0;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isTrue();
	}

	@Test
	void shouldProcessErrandErrandListSizeZero() {
		// Arrange
		final var qualifiedItems = 1;
		final var errandListSize = 0;
		final var firstErrand = 1;
		final var numOfErrands = 1;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isTrue();
	}

	@Test
	void shouldProcessErrandQualifiedItemsZero() {
		// Arrange
		final var qualifiedItems = 0;
		final var errandListSize = 1;
		final var firstErrand = 1;
		final var numOfErrands = 1;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isFalse();
	}

	@Test
	void shouldProcessErrandAllZero() {
		// Arrange
		final var qualifiedItems = 0;
		final var errandListSize = 0;
		final var firstErrand = 0;
		final var numOfErrands = 0;

		// Act & Assert
		assertThat(ValidationUtil.shouldProcessErrand(qualifiedItems, errandListSize, firstErrand, numOfErrands)).isTrue();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckValid() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckDenied() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecision(OEP_ERRAND_SKOLSKJUTS_DENIED);
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckNoDaycarePlacement() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDaycarePlacement(null);
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckNoMinorIdentifier() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setMinorIdentifier(null);
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckNoDecisionStart() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionStart(null);
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckNoDecisionEnd() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionEnd(null);
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckTodayBeforeDecisionStart() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionStart(LocalDate.now().plusDays(1).toString());
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForDayCareCheckTodayAfterDecisionEnd() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionEnd(LocalDate.now().minusDays(1).toString());
		// Act
		final var result = isOepErrandQualifiedForDayCareCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckValid() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckDeniedSkolskjuts() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecision(OEP_ERRAND_SKOLSKJUTS_DENIED);
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckDeniedElevresa() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecision(Constants.OEP_ERRAND_ELEVRESA_DENIED);
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckTodayBeforeDecisionStart() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionStart(LocalDate.now().plusDays(1).toString());
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckTodayAfterDecisionEnd() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionEnd(LocalDate.now().minusDays(1).toString());
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckNoDecisionStart() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionStart(null);
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void isOepErrandQualifiedForRelocationCheckNoDecisionEnd() {
		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		oepErrandItem.setDecisionEnd(null);
		// Act
		final var result = ValidationUtil.isOepErrandQualifiedForRelocationCheck(oepErrandItem, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}
}
