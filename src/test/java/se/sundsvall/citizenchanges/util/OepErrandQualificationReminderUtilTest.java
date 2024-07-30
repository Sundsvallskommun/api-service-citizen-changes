package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class OepErrandQualificationReminderUtilTest {

	@Test
	void isOepErrandQualifiedWhenEndDateOfDecisionIsBeforeAutumStartOfSchoolDate() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234", LocalDate.now().withMonth(8).withDayOfMonth(30));
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void isOepErrandQualifiedWhenEndDateOfDecisionIsEqualToAutumStartOfSchoolDate() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234", LocalDate.now().withMonth(8).withDayOfMonth(31));
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void isOepErrandQualifiedWhenEndDateOfDecisionIsAfterAutumStartOfSchoolDate() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234", LocalDate.now().withMonth(9).withDayOfMonth(1));
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now().withMonth(5));
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualifiedDecisionDenied() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setDecision("Avslås");
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_MinorProtected() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setMinorName("Skyddad");
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_minorIdentifierMissing() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", null);
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_minorNameMissing() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setMinorName(null);
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_DateSpanNotValid() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setDecisionEnd(LocalDate.now().minusDays(1).toString());
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_minorAgeTooOld() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "20071011234");
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_validAfterAugust() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setDecisionStart(LocalDate.of(2024, 2, 1).toString());
		errand.setDecisionEnd(LocalDate.of(2024, 9, 1).toString());
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.of(2024, 5, 1));
		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void isOepErrandQualified_notQualified_invalidDateSpan() {
		// Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.setDecisionEnd(null);
		// Act
		final var result = OepErrandQualificationReminderUtil.isOepErrandQualified(errand, LocalDate.now());
		// Assert
		assertThat(result).isFalse();
	}

}
