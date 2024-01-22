package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.citizenchanges.TestDataFactory.buildCitizen;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.citizenchanges.api.model.FamilyType;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

	@InjectMocks
	private ItemMapper itemMapper;

	@Test
	void composeInvestigationItem() {
		// Arrange
		final var citizenInfo = buildCitizen("someUuid");
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		// Act
		final var composeInvestigationItem = itemMapper.composeInvestigationItem(citizenInfo, oepErrandItem, FamilyType.SKOLSKJUTS, "someApplicantIdentifier", true);
		// Assert
		assertThat(composeInvestigationItem).isNotNull().hasNoNullFieldsOrProperties();
	}


	@Test
	void composeInvestigationItemWithMinorIdentifier() {
		// Arrange
		final var citizenInfo = buildCitizen("someUuid");
		final var oepErrandItem = buildOepErrandItem("Ja", "someApplicantIdentifier", "someMinorIdentifier");
		// Act
		final var composeInvestigationItem = itemMapper.composeInvestigationItem(citizenInfo, oepErrandItem, FamilyType.SKOLSKJUTS, "someMinorIdentifier", false);
		// Assert
		assertThat(composeInvestigationItem).isNotNull().hasNoNullFieldsOrPropertiesExcept("nameGuardian");
	}

}
