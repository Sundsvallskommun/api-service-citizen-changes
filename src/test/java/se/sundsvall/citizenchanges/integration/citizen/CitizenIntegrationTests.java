package se.sundsvall.citizenchanges.integration.citizen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildCitizen;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;

import generated.se.sundsvall.citizen.CitizenAddress;

@ExtendWith(MockitoExtension.class)
class CitizenIntegrationTests {

	@Mock
	private CitizenClient mockCitizenClient;

	@InjectMocks
	private CitizenIntegration citizenIntegration;

	@Test
	void getAddressChanges_ok() {
		// Arrange
		final var citizen = buildCitizen("someApplicantIdentifier");

		// Mock
		when(mockCitizenClient.getAddressChanges(any(String.class))).thenReturn(Set.of(citizen));

		// Act
		final var result = citizenIntegration.getAddressChanges("someDate");

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
		verify(mockCitizenClient, times(1)).getAddressChanges(any(String.class));
		verifyNoMoreInteractions(mockCitizenClient);
	}

	private void assertAddressValues(CitizenAddress a) {
		assertThat(a.getStatus()).isEqualTo("Current");
		assertThat(a.getNrDate()).isCloseTo(LocalDateTime.now(), within(3L, ChronoUnit.SECONDS));
		assertThat(a.getAddress()).isEqualTo("someAddress");
		assertThat(a.getCo()).isEqualTo("someCo");
		assertThat(a.getAppartmentNumber()).isEqualTo("someApartmentNumber");
		assertThat(a.getPostalCode()).isEqualTo("somePostalCode");
		assertThat(a.getCity()).isEqualTo("some");
		assertThat(a.getCounty()).isEqualTo("81");
		assertThat(a.getMunicipality()).isEqualTo("22");
		assertThat(a.getAddressType()).isEqualTo("someAddressType");
		assertThat(a.getxCoordLocal()).isEqualTo(12D);
		assertThat(a.getyCoordLocal()).isEqualTo(14D);
	}

	@Test
	void getAddressChanges_error() {

		// Mock
		when(mockCitizenClient.getAddressChanges(any(String.class)))
			.thenThrow(Problem.builder().build());
		// Act
		final var result = citizenIntegration.getAddressChanges("someDate");
		// Assert
		assertThat(result).isEqualTo(Set.of());
		// Verify
		verify(mockCitizenClient, times(1)).getAddressChanges(any(String.class));
		verifyNoMoreInteractions(mockCitizenClient);
	}

}
