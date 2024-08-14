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
		final var result = citizenIntegration.getAddressChanges("2281", "someDate");

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

	private void assertAddressValues(final CitizenAddress adress) {
		assertThat(adress.getStatus()).isEqualTo("Current");
		assertThat(adress.getNrDate()).isCloseTo(LocalDateTime.now(), within(3L, ChronoUnit.SECONDS));
		assertThat(adress.getAddress()).isEqualTo("someAddress");
		assertThat(adress.getCo()).isEqualTo("someCo");
		assertThat(adress.getAppartmentNumber()).isEqualTo("someApartmentNumber");
		assertThat(adress.getPostalCode()).isEqualTo("somePostalCode");
		assertThat(adress.getCity()).isEqualTo("some");
		assertThat(adress.getCounty()).isEqualTo("81");
		assertThat(adress.getMunicipality()).isEqualTo("22");
		assertThat(adress.getAddressType()).isEqualTo("someAddressType");
		assertThat(adress.getxCoordLocal()).isEqualTo(12D);
		assertThat(adress.getyCoordLocal()).isEqualTo(14D);
	}

	@Test
	void getAddressChanges_error() {

		// Mock
		when(mockCitizenClient.getAddressChanges(any(String.class)))
			.thenThrow(Problem.builder().build());
		// Act
		final var result = citizenIntegration.getAddressChanges("2281", "someDate");
		// Assert
		assertThat(result).isEqualTo(Set.of());
		// Verify
		verify(mockCitizenClient, times(1)).getAddressChanges(any(String.class));
		verifyNoMoreInteractions(mockCitizenClient);
	}

}
