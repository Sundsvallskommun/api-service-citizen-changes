package se.sundsvall.citizenchanges.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.util.MessageMapper;

import generated.se.sundsvall.messaging.DeliveryResult;
import generated.se.sundsvall.messaging.MessageResult;
import generated.se.sundsvall.messaging.MessageStatus;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

	@Mock
	private OpenEIntegration openEIntegrationMock;

	@Mock
	private MessagingClient messagingClientMock;

	@Mock
	private MessageMapper mapperMock;

	@Mock
	private ServiceProperties propertiesMock;

	@InjectMocks
	private ReminderService service;


	@Test
	void runBatch() {
		// Arrange
		// Mock
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenReturn(buildOepErrandItem("Ja", "P1", "202301011234"));
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.composeReminderReportHtmlContent(any(), any())).thenReturn("html");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));
		when(messagingClientMock.sendSms(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));
		// Act
		final var result = service.runBatch(0, 1, true);
		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(mapperMock).composeReminderContentEmail(any());
		verify(messagingClientMock).sendSms(any());
		verify(mapperMock).composeReminderContentSMS(any(), any());
		verify(mapperMock).composeSmsRequest(any(), any());
		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);
	}

	@Test
	void runBatch_dryRun() {
		// Arrange
		// Mock
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenReturn(buildOepErrandItem("Ja", "P1", "202301011234"));
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.composeReminderReportHtmlContent(any(), any())).thenReturn("html");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));
		// Act
		final var result = service.runBatch(0, 1, false);
		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(messagingClientMock).sendEmail(any());
		verify(mapperMock).composeEmailRequest(any(), any(), any(), any());

		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);

	}

	@Test
	void runBatch_SpecifyEmailAndSms() {
		// Arrange
		// Mock
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenReturn(buildOepErrandItem("Ja", "P1", "202301011234"));
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.composeReminderReportHtmlContent(any(), any())).thenReturn("html");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));
		// Act
		final var result = service.runBatch(0, 1, "076-1234567", "email");
		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(mapperMock).composeSmsRequest(any(), any());
		verify(messagingClientMock).sendSms(any());
		verify(mapperMock).composeReminderContentSMS(any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(mapperMock).composeReminderContentEmail(any());

		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);

	}

	@Test
	void runBatch_failedToSendEmailAndSms() {
		// Mock
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenReturn(buildOepErrandItem("Ja", "P1", "202301011234"));
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.composeReminderReportHtmlContent(any(), any())).thenReturn("html");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.FAILED))));
		when(messagingClientMock.sendSms(any())).thenThrow(new RuntimeException("Some exception"));
		// Act
		final var result = service.runBatch(0, 1, true);
		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(mapperMock).composeReminderContentEmail(any());
		verify(messagingClientMock).sendSms(any());
		verify(mapperMock).composeReminderContentSMS(any(), any());
		verify(mapperMock).composeSmsRequest(any(), any());
		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);

	}

	@Test
	void runBatch_emailThrowsExceptionAndBadlyFormattedSms() {
		//Arrange
		final var errand = buildOepErrandItem("Ja", "P1", "202301011234");
		errand.getContactInfo().setPhoneNumber("abc");
		// Mock
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenReturn(errand);
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.composeReminderReportHtmlContent(any(), any())).thenReturn("html");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any()))
			.thenThrow(new RuntimeException("Some exception"))
			.thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));

		// Act
		final var result = service.runBatch(0, 1, true);
		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(messagingClientMock, times(2)).sendEmail(any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(mapperMock, times(2)).composeEmailRequest(any(), any(), any(), any());
		verify(mapperMock).composeReminderContentEmail(any());
		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);

	}

	@Test
	void runBatch_getOepErrandThrowException() {

		// Mock
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any())).thenReturn(List.of("1"));
		when(openEIntegrationMock.getErrand(any(), any())).thenThrow(new RuntimeException("Some exception"));
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));

		// Act
		final var result = service.runBatch(0, 1, true);

		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(propertiesMock).familyId();
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(mapperMock).getEmailRecipients(any());
		verify(messagingClientMock).sendEmail(any());
		verify(mapperMock).composeReminderReportHtmlContent(any(), any());
		verify(mapperMock).composeEmailRequest(any(), any(), any(), any());
		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);
	}

	@Test
	void runBatch_noOepErrandsFound() {
		// Mock
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[]{"someemail@test.se"});
		when(messagingClientMock.sendEmail(any())).thenReturn(new MessageResult().messageId(UUID.randomUUID()).deliveries(List.of(new DeliveryResult().status(MessageStatus.SENT))));

		// Act
		final var result = service.runBatch(0, 1, true);

		// Assert & Verify
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);
		verify(propertiesMock).familyId();
		verify(mapperMock).getEmailRecipients(any());
		verify(messagingClientMock).sendEmail(any());
		verify(openEIntegrationMock).getErrandIds(any(), any(), any(), any());
		verify(mapperMock).composeReminderReportHtmlContent(any(), any());
		verify(mapperMock).composeEmailRequest(any(), any(), any(), any());
		verifyNoMoreInteractions(openEIntegrationMock, messagingClientMock, mapperMock, propertiesMock);
	}


}
