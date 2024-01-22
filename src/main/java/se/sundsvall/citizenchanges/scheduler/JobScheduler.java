package se.sundsvall.citizenchanges.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import se.sundsvall.citizenchanges.service.RelocationCheckService;

@Configuration
@EnableScheduling
public class JobScheduler {

	private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

	private final RelocationCheckService relocationCheckService;

	public JobScheduler(final RelocationCheckService relocationCheckService) {
		this.relocationCheckService = relocationCheckService;
	}

	@Scheduled(cron = "${scheduling.expression}")
	void startRelocationCheck() {

		final LocalDateTime thisTime = LocalDateTime.now();
		log.info("Scheduler executed at {}. About to run batch job for relocation check...", thisTime);
		final var result = relocationCheckService.runBatch();
		log.info("Batch job is done. {}", result);
	}

}
