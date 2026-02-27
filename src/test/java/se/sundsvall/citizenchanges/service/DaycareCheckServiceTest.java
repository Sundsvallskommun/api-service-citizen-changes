package se.sundsvall.citizenchanges.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.api.model.DaycareInvestigationItem;
import se.sundsvall.citizenchanges.integration.messaging.MessagingClient;
import se.sundsvall.citizenchanges.integration.opene.OpenEIntegration;
import se.sundsvall.citizenchanges.scheduler.FileHandler;
import se.sundsvall.citizenchanges.util.MessageMapper;
import se.sundsvall.dept44.problem.Problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static se.sundsvall.citizenchanges.TestDataFactory.buildOepErrandItem;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_DECIDED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_GRANTED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION;

@ExtendWith(MockitoExtension.class)
class DaycareCheckServiceTest {

	@Mock
	private OpenEIntegration openEIntegrationMock;

	@Mock
	private MessagingClient messagingIntegrationMock;

	@Mock
	private MessageMapper mapperMock;

	@Mock
	private FileHandler fileHandlerMock;

	@Mock
	private ServiceProperties propertiesMock;

	@InjectMocks
	private DaycareCheckService service;

	@Test
	void runBatchWithFileShouldReturnBatchStatusDone() throws IOException {

		// Arrange
		final var path = Paths.get("src/test/resources/mockfiles/mockfile.xls");
		final var name = "mockfile.xls";
		final var originalFileName = "mockfile.xls";
		final var contentType = "text/plain";
		final var content = Files.readAllBytes(path);
		final var file = new MockMultipartFile(name, originalFileName, contentType, content);

		// Mock
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});
		when(openEIntegrationMock.getErrandIds(any(), any(), any(), any()))
			.thenReturn(List.of("someErrandId", "someErrandId2"));
		when(openEIntegrationMock.getErrand(any(), any()))
			.thenReturn(buildOepErrandItem("Ja", "P1", "M1"));
		when(fileHandlerMock.getISTPlacement(any())).thenReturn(DaycareInvestigationItem.builder().build());

		// Act
		final var result = service.runBatch(0, 1, 1, file, "2281");

		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);

		verify(propertiesMock).familyId();
		verify(mapperMock).getEmailRecipients(any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrand(any(), any());
		verify(fileHandlerMock).getISTPlacement(any());
		verify(fileHandlerMock).parse(any());
		verify(messagingIntegrationMock).sendEmail(any(String.class), any());
		verify(mapperMock).composeDaycareReportHtmlContent(any(), any());
		verify(mapperMock).composeEmailRequest(any(), any(), any(), any());

		verifyNoMoreInteractions(openEIntegrationMock, messagingIntegrationMock, mapperMock, fileHandlerMock, propertiesMock);
	}

	@Test
	void runBatchNoOepErrands() throws IOException {

		// Arrange
		final var path = Paths.get("src/test/resources/mockfiles/mockfile.xls");
		final var name = "mockfile.xls";
		final var originalFileName = "mockfile.xls";
		final var contentType = "text/plain";
		final var content = Files.readAllBytes(path);
		final var file = new MockMultipartFile(name, originalFileName, contentType, content);

		// Mock
		when(propertiesMock.familyId()).thenReturn("344,349");
		when(mapperMock.getEmailRecipients(any())).thenReturn(new String[] {
			"someemail@test.se"
		});

		// Act
		final var result = service.runBatch(0, 1, 1, file, "2281");

		// Assert
		assertThat(result).isNotNull().isEqualTo(BatchStatus.DONE);

		verify(propertiesMock, times(1)).familyId();
		verify(mapperMock).getEmailRecipients(any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_DECIDED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED.toLowerCase()), any(), any());
		verify(openEIntegrationMock).getErrandIds(any(), eq(OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION.toLowerCase()), any(), any());
		verify(fileHandlerMock).parse(any());
		verify(messagingIntegrationMock).sendEmail(any(String.class), any());
		verify(mapperMock).composeDaycareReportHtmlContent(any(), any());
		verify(mapperMock).composeEmailRequest(any(), any(), any(), any());

		verifyNoMoreInteractions(openEIntegrationMock, messagingIntegrationMock, mapperMock, fileHandlerMock, propertiesMock);
	}

	@Test
	void deleteCachedFile() {

		service.deleteCachedFile();

		// Assert
		verify(fileHandlerMock).deleteCachedFile();
		verifyNoMoreInteractions(fileHandlerMock);
		verifyNoInteractions(openEIntegrationMock, messagingIntegrationMock, mapperMock, propertiesMock);
	}

	@Test
	void deleteCachedFileException() {

		// Mock
		doThrow(Problem.valueOf(INTERNAL_SERVER_ERROR, "Some exception")).when(fileHandlerMock).deleteCachedFile();

		assertThatThrownBy(() -> service.deleteCachedFile())
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Some exception");

		// Assert
		verify(fileHandlerMock).deleteCachedFile();
		verifyNoMoreInteractions(fileHandlerMock);
		verifyNoInteractions(openEIntegrationMock, messagingIntegrationMock, mapperMock, propertiesMock);
	}

	@Test
	void checkCachedFile() {
		// Mock
		when(fileHandlerMock.checkCachedFile()).thenReturn(true);

		// Act
		final var result = service.checkCachedFile();

		// Assert
		assertThat(result).isTrue();
		verify(fileHandlerMock).checkCachedFile();
		verifyNoMoreInteractions(fileHandlerMock);
		verifyNoInteractions(openEIntegrationMock, messagingIntegrationMock, mapperMock, propertiesMock);
	}

}
