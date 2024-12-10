package se.sundsvall.citizenchanges.api;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.citizenchanges.Application;
import se.sundsvall.citizenchanges.service.ReminderService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class ReminderResourceTests {

	private static final String MUNICIPALITY_ID = "2281";

	private static final String PATH = "/" + MUNICIPALITY_ID + "/reminder/batchtrigger/reminder/endofterm";

	@MockitoBean
	private ReminderService reminderService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void runReminderBatch() {
		webTestClient.get()
			.uri(PATH + "?firstErrand=1&numOfErrands=1&sendMessage=true")
			.exchange()
			.expectStatus()
			.isOk();

		verify(reminderService).runBatch(anyInt(), anyInt(), anyBoolean(), anyString());
		verifyNoMoreInteractions(reminderService);
	}

	@Test
	void runReminderBatchWithOpenEList() {
		webTestClient.post()
			.uri(PATH + "?firstErrand=1&numOfErrands=1&backtrackDays=1d&sendMessage=true")
			.bodyValue(List.of(""))
			.exchange()
			.expectStatus()
			.isOk();

		verify(reminderService).runBatch(anyInt(), anyInt(), anyBoolean(), anyList(), isNull(), isNull(), anyString());
		verifyNoMoreInteractions(reminderService);
	}

	@Test
	void runReminderBatchWithStringParams() {
		webTestClient.get()
			.uri(PATH + "/dryrun?firstErrand=1&numOfErrands=1&sms=somePhoneNumber&email=someEmail@localhost.se")
			.exchange()
			.expectStatus()
			.isOk();

		verify(reminderService).runBatch(anyInt(), anyInt(), anyString(), anyString(), anyString());
		verifyNoMoreInteractions(reminderService);
	}

}
