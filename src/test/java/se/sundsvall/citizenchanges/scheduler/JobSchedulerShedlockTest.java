package se.sundsvall.citizenchanges.scheduler;

import static java.time.Clock.systemUTC;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.citizenchanges.service.RelocationCheckService;

@SpringBootTest(properties = {
	"scheduling.expression=*/5 * * * * *", // Setup to execute every five seconds
	"spring.flyway.enabled=true",
	"integration.db.case-status.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
	"integration.db.case-status.url=jdbc:tc:mariadb:10.6.4:////",
	"server.shutdown=immediate",
	"spring.lifecycle.timeout-per-shutdown-phase=0s"
})
@ActiveProfiles("junit")
class JobSchedulerShedlockTest {

	@TestConfiguration
	public static class ShedlockTestConfiguration {
		@Bean
		@Primary
		public RelocationCheckService createMock() {
			return Mockito.mock(RelocationCheckService.class);
		}
	}

	@Autowired
	private RelocationCheckService relocationCheckServiceMock;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private LocalDateTime mockCalledTime;

	@Test
	void verifyShedLock() {

		// Let mock hang
		doAnswer( invocation -> {
			mockCalledTime = LocalDateTime.now();
			await().forever()
				.until(() -> false);
			return null;
		}).when(relocationCheckServiceMock).runBatch();

		// Make sure scheduling occurs multiple times
		await().until(() -> mockCalledTime != null && mockCalledTime.isBefore(mockCalledTime.plusSeconds(2)));

		// Verify lock
		await().atMost(5, SECONDS)
			.untilAsserted(() -> assertThat(getLockedAt())
				.isCloseTo(LocalDateTime.now(systemUTC()), within(10, ChronoUnit.SECONDS)));

		// Only one call should be made as long as runBatch() is locked and mock is waiting for first call to finish
		verify(relocationCheckServiceMock).runBatch();
		verifyNoMoreInteractions(relocationCheckServiceMock);
	}

	private LocalDateTime getLockedAt() {
		return jdbcTemplate.query(
			"SELECT locked_at FROM shedlock WHERE name = :name",
			Map.of("name", "relocation_check"),
			this::mapTimestamp);
	}

	private LocalDateTime mapTimestamp(final ResultSet rs) throws SQLException {
		if (rs.next()) {
			return LocalDateTime.parse(rs.getString("locked_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		}
		return null;
	}
}
