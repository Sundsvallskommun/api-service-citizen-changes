package se.sundsvall.citizenchanges.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.citizenchanges.Application;
import se.sundsvall.citizenchanges.integration.citizen.CitizenIntegration;
import se.sundsvall.citizenchanges.service.RelocationCheckService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class RelocationCheckResourceTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String PATH = "/" + MUNICIPALITY_ID + "/relocations/batchtrigger/relocations";
	private static final String PATH_RECENT_MOVES = "/" + MUNICIPALITY_ID + "/relocations/meta/recentmoves?backtrackDays%s";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private CitizenIntegration citizenIntegration;

	@MockitoBean
	private RelocationCheckService relocationCheckService;

	@Test
	void runBatch() {

		webTestClient
			.get()
			.uri(PATH + "?backtrackDays=1")
			.exchange()
			.expectStatus()
			.isOk();

		verify(relocationCheckService).runBatch(any(), isNull(), any());
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

		verify(relocationCheckService).runBatch(any(), anySet(), any());
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
			.uri(PATH_RECENT_MOVES.formatted("=1"))
			.exchange()
			.expectStatus()
			.isOk();

		verify(citizenIntegration).getAddressChanges(any(), any());
	}

	@Test
	void getRecentMovesWithInvalidDate() {
		webTestClient
			.get()
			.uri(PATH_RECENT_MOVES.formatted("=a"))
			.exchange()
			.expectStatus()
			.isBadRequest();

		verifyNoInteractions(citizenIntegration);
	}

}
