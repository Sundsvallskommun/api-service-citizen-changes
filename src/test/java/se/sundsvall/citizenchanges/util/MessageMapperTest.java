package se.sundsvall.citizenchanges.util;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.citizenchanges.api.model.FamilyType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.TestDataFactory.buildDaycareInvestigationItem;
import static se.sundsvall.citizenchanges.TestDataFactory.buildInvestigationItem;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;
import static se.sundsvall.citizenchanges.TestDataFactory.buildReportMetaData;
import static se.sundsvall.citizenchanges.util.Constants.EMAIL_SENDER_ADDRESS;
import static se.sundsvall.citizenchanges.util.Constants.REMINDER_SMS_SENDER;

@ExtendWith(MockitoExtension.class)
class MessageMapperTest {

	@Mock
	private MessageMapperProperties messageMapperProperties;

	@InjectMocks
	private MessageMapper messageMapper;

	@Test
	void composeEmailRequest() {

		// Arrange
		final var payload = "";
		final var emailRecipient = "someEmail@localhost.se";
		final var subject = "someSubject";
		final var sender = "someSender";

		// Act
		final var emailRequest = messageMapper.composeEmailRequest(payload, emailRecipient, sender, subject);

		// Assert
		assertThat(emailRequest).isNotNull();
		assertThat(emailRequest.getSubject()).isEqualTo(subject);
		assertThat(emailRequest.getEmailAddress()).isEqualTo(emailRecipient);
		assertThat(emailRequest.getSender().getName()).isEqualTo(sender);
		assertThat(emailRequest.getSender().getAddress()).isEqualTo(EMAIL_SENDER_ADDRESS);
		assertThat(emailRequest.getHtmlMessage()).isEqualTo(payload);

	}

	@Test
	void composeSmsRequest() {

		// Arrange
		final var payload = "";
		final var phoneNumber = "somePhoneNumber";

		// Act
		final var smsRequest = messageMapper.composeSmsRequest(payload, phoneNumber);

		// Assert
		assertThat(smsRequest).isNotNull();
		assertThat(smsRequest.getMessage()).isEqualTo(payload);
		assertThat(smsRequest.getMobileNumber()).isEqualTo(phoneNumber);
		assertThat(smsRequest.getSender()).isEqualTo(REMINDER_SMS_SENDER);
	}

	@Test
	void getImageData() throws IOException {

		// Act
		final var imageData = messageMapper.getImageData("/images/logo.png");

		// Assert
		assertThat(imageData).isNotNull();
	}

	@Test
	void getImageDataShouldThrowException() {
		assertThatThrownBy(() -> messageMapper.getImageData("/images/doesnotexist.png"))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void getEmailRecipients() {

		// Mock
		when(messageMapperProperties.recipientsSkolskjuts()).thenReturn("someRecipientSkolskjuts");

		// Act
		final var recipients = messageMapper.getEmailRecipients(FamilyType.SKOLSKJUTS);

		// Assert
		assertThat(recipients).isNotNull().isNotEmpty().hasSize(1).contains("someRecipientSkolskjuts");
		verifyNoMoreInteractions(messageMapperProperties);
	}

	@Test
	void getEmailRecipientsShouldReturnEmptyList() {

		// Mock
		when(messageMapperProperties.recipientsSkolskjuts()).thenReturn("");

		// Act
		final var recipients = messageMapper.getEmailRecipients(FamilyType.SKOLSKJUTS);

		// Assert
		assertThat(recipients).isNotNull().hasSize(1);
		assertThat(recipients[0]).isEmpty();
		verifyNoMoreInteractions(messageMapperProperties);
	}

	@ParameterizedTest
	@EnumSource(FamilyType.class)
	void composeHtmlContent(final FamilyType familyType) {

		// Arrange
		final var reportMetaData = buildReportMetaData();
		final var investigationItem = buildInvestigationItem();

		// Mock
		when(messageMapperProperties.linkTemplate()).thenReturn("http://link.template");

		// Act
		final var htmlContent = messageMapper.composeHtmlContent(familyType, List.of(investigationItem), reportMetaData);

		// Assert
		assertThat(htmlContent).isNotNull().isNotEmpty();
		verifyNoMoreInteractions(messageMapperProperties);
	}

	@Test
	void composeDaycareReportHtmlContent() {

		// Arrange
		final var reportMetaData = buildReportMetaData();
		final var daycare = buildDaycareInvestigationItem();

		// Mock
		when(messageMapperProperties.linkTemplate()).thenReturn("http://link.template");

		// Act
		final var daycareReportHtmlContent = messageMapper.composeDaycareReportHtmlContent(List.of(daycare), reportMetaData);

		// Assert
		assertThat(daycareReportHtmlContent).isNotNull().isNotEmpty();
		verifyNoMoreInteractions(messageMapperProperties);
	}

	@Test
	void composeReminderReportHtmlContent() {

		// Arrange
		final var reportMetaData = buildReportMetaData();
		final var oepErrandItem = buildOepErrandItem("Ja", "P1", "M1");

		// Mock
		when(messageMapperProperties.linkTemplate()).thenReturn("http://link.template");

		// Act
		final var composeReminderReportHtmlContent = messageMapper.composeReminderReportHtmlContent(List.of(oepErrandItem), reportMetaData);

		// Assert
		assertThat(composeReminderReportHtmlContent).isNotNull().isNotEmpty();
		verifyNoMoreInteractions(messageMapperProperties);
	}

	@Test
	void composeReminderContentEmail() {

		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "P1", "M1");

		// Act
		final var composeReminderContentEmail = messageMapper.composeReminderContentEmail(oepErrandItem);

		// Assert
		assertThat(composeReminderContentEmail).isNotNull().isNotEmpty();
		verifyNoInteractions(messageMapperProperties);
	}

	@Test
	void composeReminderContentSMS() {

		// Arrange
		final var oepErrandItem = buildOepErrandItem("Ja", "P1", "M1");

		// Act
		final var composeReminderContentSMS = messageMapper.composeReminderContentSMS(oepErrandItem, "2021");

		// Assert
		assertThat(composeReminderContentSMS).isNotNull().isNotEmpty();
		verifyNoInteractions(messageMapperProperties);
	}

}
