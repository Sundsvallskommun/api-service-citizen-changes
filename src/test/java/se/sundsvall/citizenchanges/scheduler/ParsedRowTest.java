package se.sundsvall.citizenchanges.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ParsedRowTest {

	@Test
	void testBuilderMethods() {
		// Arrange
		final var personId = "someChangeStart";
		final var unit = "someChangeEnd";
		final var placementStart = "somePlacementStart";
		final var placementEnd = "somePlacementEnd";
		final var changeStart = "someChangeStart";
		final var taxCategory = "someTaxCategory";
		final var calculatingFounder = "someCalculatingFounder";
		final var guardian1 = "someGuardian1";
		final var guardian2 = "someGuardian2";

		// Act
		final var contactInfo = ParsedRow.builder()
			.withPersonId(personId)
			.withUnit(unit)
			.withPlacementStart(placementStart)
			.withPlacementEnd(placementEnd)
			.withChangeStart(changeStart)
			.withTaxCategory(taxCategory)
			.withCalculatingFounder(calculatingFounder)
			.withGuardian1(guardian1)
			.withGuardian2(guardian2)
			.build();

		// Assert
		assertThat(contactInfo).isNotNull().hasNoNullFieldsOrProperties().extracting(
			"personId",
			"unit",
			"placementStart",
			"placementEnd",
			"changeStart",
			"taxCategory",
			"calculatingFounder",
			"guardian1",
			"guardian2").containsExactly(
			personId,
			unit,
			placementStart,
			placementEnd,
			changeStart,
			taxCategory,
			calculatingFounder,
			guardian1,
			guardian2);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new ParsedRow()).hasAllNullFieldsOrProperties();
		assertThat(ParsedRow.builder().build()).hasAllNullFieldsOrProperties();
	}

}
