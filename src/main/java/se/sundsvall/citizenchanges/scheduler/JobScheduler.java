package se.sundsvall.citizenchanges.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import se.sundsvall.citizenchanges.service.RelocationCheckService;

@Component
public class JobScheduler {

	private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

	private final RelocationCheckService relocationCheckService;

	public JobScheduler(final RelocationCheckService relocationCheckService) {
		this.relocationCheckService = relocationCheckService;
	}

	@Scheduled(cron = "${scheduling.expression}")
	@SchedulerLock(name = "relocation_check", lockAtMostFor = "${scheduling.lock-at-most-for}")
	void startRelocationCheck() {
		final var thisTime = LocalDateTime.now();
		log.info("Scheduler executed at {}. About to run batch job for relocation check...", thisTime);
		final var result = relocationCheckService.runBatch();
		log.info("Batch job is done. {}", result);
	}

}
