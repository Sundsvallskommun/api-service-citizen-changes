package se.sundsvall.citizenchanges.integration.citizen;

import generated.se.sundsvall.citizen.CitizenAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildCitizen;

@ExtendWith(MockitoExtension.class)
class CitizenIntegrationTests {

	@Mock
	private CitizenClient mockCitizenClient;

	@InjectMocks
	private CitizenIntegration citizenIntegration;

	@Test
	void getAddressChangesOK() {

		// Arrange
		final var municipalityId = "2281";
		final var citizen = buildCitizen("someApplicantIdentifier");

		// Mock
		when(mockCitizenClient.getAddressChanges(eq(municipalityId), any(String.class))).thenReturn(Set.of(citizen));

		// Act
		final var result = citizenIntegration.getAddressChanges(municipalityId, "someDate");

		// Assert
		assertThat(result).isNotNull().isNotEmpty();
		final var resultItem = result.iterator().next();
		assertThat(resultItem).isNotNull();
		assertThat(resultItem.getGender()).isEqualTo("someGender");
		assertThat(resultItem.getClassified()).isEqualTo("someClassified");
		assertThat(resultItem.getPersonId()).isNotNull();
		assertThat(resultItem.getLastname()).isEqualTo("someLastname");
		assertThat(resultItem.getGivenname()).isEqualTo("someGivenName");
		assertThat(resultItem.getPersonNumber()).isEqualTo("someApplicantIdentifier");
		assertThat(resultItem.getCustodianFor().getFirst()).isNotNull();
		assertThat(resultItem.getCustodianFor().getFirst().getPersonnumber()).isEqualTo("somePersonnummer");
		assertThat(resultItem.getCustodianFor().getFirst().getTypeOfSchool()).isEqualTo("someTypeOfSchool");
		assertThat(resultItem.getAddresses().getFirst()).isNotNull().satisfies(this::assertAddressValues);

		// Verify
		verify(mockCitizenClient, times(1)).getAddressChanges(eq(municipalityId), any(String.class));
		verifyNoMoreInteractions(mockCitizenClient);
	}

	private void assertAddressValues(final CitizenAddress address) {
		assertThat(address.getStatus()).isEqualTo("Current");
		assertThat(address.getNrDate()).isCloseTo(LocalDateTime.now(), within(3L, ChronoUnit.SECONDS));
		assertThat(address.getAddress()).isEqualTo("someAddress");
		assertThat(address.getCo()).isEqualTo("someCo");
		assertThat(address.getAppartmentNumber()).isEqualTo("someApartmentNumber");
		assertThat(address.getPostalCode()).isEqualTo("somePostalCode");
		assertThat(address.getCity()).isEqualTo("some");
		assertThat(address.getCounty()).isEqualTo("81");
		assertThat(address.getMunicipality()).isEqualTo("22");
		assertThat(address.getAddressType()).isEqualTo("someAddressType");
		assertThat(address.getxCoordLocal()).isEqualTo(12D);
		assertThat(address.getyCoordLocal()).isEqualTo(14D);
	}

	@Test
	void getAddressChangesNOK() {

		// Arrange
		final var municipalityId = "2281";

		when(mockCitizenClient.getAddressChanges(eq(municipalityId), any(String.class))).thenThrow(Problem.builder().build());

		// Act
		final var result = citizenIntegration.getAddressChanges(municipalityId, "someDate");

		// Assert
		assertThat(result).isEqualTo(Set.of());
		// Verify
		verify(mockCitizenClient, times(1)).getAddressChanges(eq(municipalityId), any(String.class));
		verifyNoMoreInteractions(mockCitizenClient);
	}
}
