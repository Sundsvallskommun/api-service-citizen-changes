package se.sundsvall.citizenchanges.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import se.sundsvall.citizenchanges.Application;
import se.sundsvall.citizenchanges.integration.citizen.CitizenIntegration;
import se.sundsvall.citizenchanges.service.RelocationCheckService;


@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class RelocationCheckResourceTest {

	private static final String PATH = "/relocations/batchtrigger/relocations";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private CitizenIntegration citizenIntegration;

	@MockBean
	private RelocationCheckService relocationCheckService;

	@Test
	void runBatch() {

		webTestClient
			.get()
			.uri(PATH + "?backtrackDays=1")
			.exchange()
			.expectStatus()
			.isOk();

		verify(relocationCheckService).runBatch(any(), isNull());
	}

	@Test
	void runBatchWithInvalidDate() {
		webTestClient
			.get()
			.uri(PATH + "?backtrackDays=a")
			.exchange()
			.expectStatus()
			.isBadRequest();

		verifyNoInteractions(relocationCheckService);
	}

	@Test
	void runBatchWithCitizenWithChangedAddresses() {

		final var citizenWithChangedAddresses = Set.of(new CitizenWithChangedAddress());

		webTestClient
			.post()
			.uri(PATH + "?backtrackDays=1")
			.bodyValue(citizenWithChangedAddresses)
			.exchange()
			.expectStatus()
			.isOk();

		verify(relocationCheckService).runBatch(any(), anySet());
	}

	@Test
	void runBatchWithCitizenWithChangedAddressesAndInvalidDate() {

		final var citizenWithChangedAddresses = Set.of(new CitizenWithChangedAddress());

		webTestClient
			.post()
			.uri(PATH + "?backtrackDays=a")
			.bodyValue(citizenWithChangedAddresses)
			.exchange()
			.expectStatus()
			.isBadRequest();

		verifyNoInteractions(relocationCheckService);
	}

	@Test
	void runBatchWithInvalidCitizenWithChangedAddresses() {

		webTestClient
			.post()
			.uri(PATH + "?backtrackDays=1")
			.header("Content-Type", APPLICATION_JSON_VALUE)
			.exchange()
			.expectStatus()
			.isBadRequest();

		verifyNoInteractions(relocationCheckService);
	}

	@Test
	void getRecentMoves() {
		webTestClient
			.get()
			.uri("/relocations/meta/recentmoves?backtrackDays=1")
			.exchange()
			.expectStatus()
			.isOk();

		verify(citizenIntegration).getAddressChanges(any());
	}

	@Test
	void getRecentMovesWithInvalidDate() {
		webTestClient
			.get()
			.uri("/relocations/meta/recentmoves?backtrackDays=a")
			.exchange()
			.expectStatus()
			.isBadRequest();

		verifyNoInteractions(citizenIntegration);
	}

}
