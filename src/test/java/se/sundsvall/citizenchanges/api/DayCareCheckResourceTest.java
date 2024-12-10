package se.sundsvall.citizenchanges.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.citizenchanges.Application;
import se.sundsvall.citizenchanges.service.DaycareCheckService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class DayCareCheckResourceTest {

	private static final String MUNICIPALITY_ID = "2281";

	private static final String PATH = "/" + MUNICIPALITY_ID + "/daycare/batchtrigger/daycarechecker";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private DaycareCheckService daycareCheckService;

	@Test
	void runDaycareCheckBatch() throws IOException {

		final MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
		multipartBodyBuilder.part("file", new ByteArrayResource("test".getBytes()), MediaType.MULTIPART_FORM_DATA)
			.filename("testfile");

		webTestClient.post()
			.uri(PATH + "?firstErrand=1&numOfErrands=1&backtrackDays=1")
			.contentType(MediaType.valueOf(MULTIPART_FORM_DATA_VALUE))
			.accept(APPLICATION_JSON)
			.bodyValue(multipartBodyBuilder.build())
			.exchange()
			.expectStatus()
			.isOk();

		verify(daycareCheckService).runBatch(anyInt(), anyInt(), anyInt(), any(), anyString());
		verifyNoMoreInteractions(daycareCheckService);
	}

	@Test
	void checkCachedFile() {

		webTestClient.get()
			.uri("/" + MUNICIPALITY_ID + "/daycare/cachedFile")
			.exchange()
			.expectStatus()
			.isOk();

		verify(daycareCheckService).checkCachedFile();
		verifyNoMoreInteractions(daycareCheckService);
	}

	@Test
	void deleteCachedFile() {

		webTestClient.delete()
			.uri("/" + MUNICIPALITY_ID + "/daycare/cachedFile")
			.exchange()
			.expectStatus()
			.isOk();

		verify(daycareCheckService).deleteCachedFile();
		verifyNoMoreInteractions(daycareCheckService);
	}

}
