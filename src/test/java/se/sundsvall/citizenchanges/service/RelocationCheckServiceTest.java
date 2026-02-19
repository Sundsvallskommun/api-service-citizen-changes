package se.sundsvall.citizenchanges.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.api.model.InvestigationItem;
import se.sundsvall.citizenchanges.integration.citizen.CitizenIntegration;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.util.ItemMapper;
import se.sundsvall.citizenchanges.util.MessageMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildCitizen;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;
import static se.sundsvall.citizenchanges.util.Constants.META_BACKTRACK_DAYS_DEFAULT;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_DECIDED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_GRANTED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_READY;

@ExtendWith(MockitoExtension.class)
class RelocationCheckServiceTest {

	@Mock
	private CitizenIntegration citizenIntegrationMock;

	@Mock
	private OpenEIntegration openEIntegrationMock;

	@Mock
	private MessagingClient messagingClientMock;

	@Mock
	private MessageMapper mapperMock;

	@Mock
	private ItemMapper itemMapperMock;

	@Mock
	private ServiceProperties propertiesMock;

	@InjectMocks
	private RelocationCheckService service;

	@Test
	void runBatch() {

		// Arrange
		final var minor = buildCitizen("M2");
		minor.getAddresses().getFirst().setxCoordLocal(0D);

		final var parent = buildCitizen("P1");
		final var parent2 = buildCitizen("P2");
		parent2.setAddresses(List.of());

		final var citizen = Set.of(parent, buildCitizen("P2"), minor, buildCitizen("M1"));

		// Mock
		when(citizenIntegrationMock.getAddressChanges(any(String.class), any(String.class))).thenReturn(citizen);
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any()))
			.thenReturn(List.of("P1", "2", "3", "4"));
		when(openEIntegrationMock.getErrand(any(), any()))
			.thenReturn(buildOepErrandItem("Ja", "P1", "M1"))
			.thenReturn(buildOepErrandItem("Ja", "P2", "M2"));
		when(itemMapperMock.composeInvestigationItem(any(), any(), any(), any(), any(boolean.class)))
			.thenReturn(InvestigationItem.builder().build());

		// Act
		final var result = service.runBatch("2281");
		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(citizenIntegrationMock).getAddressChanges(any(String.class), any());

		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_READY.toLowerCase()), any(), any());
		verify(openEIntegrationMock, times(8)).getErrand(any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any(String.class), any());
		verify(propertiesMock).familyId();
		verify(mapperMock, times(2)).getEmailRecipients(any());
		verify(mapperMock, times(2)).composeHtmlContent(any(), any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(itemMapperMock, times(16)).composeInvestigationItem(any(), any(), any(), any(), any(boolean.class));
		verifyNoMoreInteractions(propertiesMock, itemMapperMock, mapperMock, citizenIntegrationMock, openEIntegrationMock, messagingClientMock);

	}

	@Test
	void runBatchNoMoves() {
		// Mock
		when(citizenIntegrationMock.getAddressChanges(any(String.class), any(String.class))).thenReturn(Collections.emptySet());
		// Act
		final var result = service.runBatch("2281");
		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.ERROR);
		verify(citizenIntegrationMock).getAddressChanges(any(String.class), any());
		verifyNoMoreInteractions(citizenIntegrationMock);
		verifyNoInteractions(propertiesMock, itemMapperMock, mapperMock, openEIntegrationMock, messagingClientMock);
	}

	@Test
	void runBatchNoOepErrands() {

		// Arrange
		final var minor = buildCitizen("M2");
		minor.getAddresses().getFirst().setxCoordLocal(0D);

		final var parent = buildCitizen("P1");
		parent.setAddresses(List.of());

		final var citizen = Set.of(parent, buildCitizen("P2"), minor, buildCitizen("M1"));

		// Mock
		when(citizenIntegrationMock.getAddressChanges(any(String.class), any(String.class))).thenReturn(citizen);
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});

		// Act
		final var result = service.runBatch("2281");

		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(citizenIntegrationMock).getAddressChanges(any(String.class), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_READY.toLowerCase()), any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any(String.class), any());
		verify(propertiesMock).familyId();
		verify(mapperMock, times(2)).getEmailRecipients(any());
		verify(mapperMock, times(2)).composeHtmlContent(any(), any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verifyNoMoreInteractions(propertiesMock, mapperMock, citizenIntegrationMock, openEIntegrationMock, messagingClientMock);
		verifyNoInteractions(itemMapperMock);
	}

	@Test
	void runBatchOepThrowsException() {

		// Arrange
		final var minor = buildCitizen("M2");
		minor.getAddresses().getFirst().setxCoordLocal(0D);

		final var parent = buildCitizen("P1");
		parent.setAddresses(List.of());

		final var citizen = Set.of(parent, buildCitizen("P2"), minor, buildCitizen("M1"));

		// Mock
		when(citizenIntegrationMock.getAddressChanges(any(String.class), any(String.class))).thenReturn(citizen);
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any()))
			.thenReturn(List.of("P1"));
		when(openEIntegrationMock.getErrand(any(), any()))
			.thenThrow(new RuntimeException("Some exception"));

		// Act
		final var result = service.runBatch("2281");

		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(citizenIntegrationMock).getAddressChanges(any(String.class), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_READY.toLowerCase()), any(), any());
		verify(openEIntegrationMock, times(2)).getErrand(any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any(String.class), any());
		verify(propertiesMock).familyId();
		verify(mapperMock, times(2)).getEmailRecipients(any());
		verify(mapperMock, times(2)).composeHtmlContent(any(), any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verifyNoMoreInteractions(propertiesMock, mapperMock, citizenIntegrationMock, openEIntegrationMock, messagingClientMock);
		verifyNoInteractions(itemMapperMock);
	}

	@Test
	void runBatchIncludeCitizenInfos() {

		// Arrange
		final var minor = buildCitizen("M2");
		minor.getAddresses().getFirst().setxCoordLocal(0D);

		final var parent = buildCitizen("P1");
		parent.setAddresses(List.of());

		final var citizen = Set.of(parent, buildCitizen("P2"), minor, buildCitizen("M1"));

		// Mock
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any()))
			.thenReturn(List.of("P1", "2", "3", "4"));
		when(openEIntegrationMock.getErrand(any(), any()))
			.thenReturn(buildOepErrandItem("Ja", "P1", "M1"))
			.thenReturn(buildOepErrandItem("Ja", "P2", "M2"));
		when(itemMapperMock.composeInvestigationItem(any(), any(), any(), any(), any(boolean.class)))
			.thenReturn(InvestigationItem.builder().build());

		// Act
		final var result = service.runBatch(Optional.of(META_BACKTRACK_DAYS_DEFAULT), citizen, "2281");
		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_READY.toLowerCase()), any(), any());
		verify(openEIntegrationMock, times(8)).getErrand(any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any(String.class), any());
		verify(propertiesMock).familyId();
		verify(mapperMock, times(2)).getEmailRecipients(any());
		verify(mapperMock, times(2)).composeHtmlContent(any(), any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(itemMapperMock, times(16)).composeInvestigationItem(any(), any(), any(), any(), any(boolean.class));
		verifyNoMoreInteractions(propertiesMock, itemMapperMock, mapperMock, openEIntegrationMock, messagingClientMock);
		verifyNoInteractions(citizenIntegrationMock);
	}
}
