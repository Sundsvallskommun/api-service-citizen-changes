package se.sundsvall.citizenchanges.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import se.sundsvall.citizenchanges.service.RelocationCheckService;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
public class JobScheduler {

	private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

	private static final String SUNDSVALL_MUNICIPALITY_ID = "2281";

	private final RelocationCheckService relocationCheckService;


	public JobScheduler(final RelocationCheckService relocationCheckService) {
		this.relocationCheckService = relocationCheckService;
	}

	@Scheduled(cron = "${scheduling.expression}")
	@SchedulerLock(name = "relocation_check", lockAtMostFor = "${scheduling.lock-at-most-for}")
	void startRelocationCheck() {
		final var thisTime = LocalDateTime.now();
		LOG.info("Scheduler executed at {}. About to run batch job for relocation check...", thisTime);
		final var result = relocationCheckService.runBatch(SUNDSVALL_MUNICIPALITY_ID);
		LOG.info("Batch job is done. {}", result);
	}

}
