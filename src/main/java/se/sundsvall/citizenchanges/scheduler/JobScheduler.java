package se.sundsvall.citizenchanges.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.service.RelocationCheckService;
import se.sundsvall.dept44.scheduling.Dept44Scheduled;
import se.sundsvall.dept44.scheduling.health.Dept44HealthUtility;

@Component
public class JobScheduler {

	private static final String SUNDSVALL_MUNICIPALITY_ID = "2281";

	private final RelocationCheckService relocationCheckService;
	private final Dept44HealthUtility dept44HealthUtility;

	@Value("${scheduling.name}")
	private String jobName;

	public JobScheduler(final RelocationCheckService relocationCheckService, final Dept44HealthUtility dept44HealthUtility) {
		this.relocationCheckService = relocationCheckService;
		this.dept44HealthUtility = dept44HealthUtility;
	}

	@Dept44Scheduled(cron = "${scheduling.expression}",
		name = "${scheduling.name}",
		lockAtMostFor = "${scheduling.lock-at-most-for}",
		maximumExecutionTime = "${scheduling.maximum-execution-time}")
	void startRelocationCheck() {
		final var result = relocationCheckService.runBatch(SUNDSVALL_MUNICIPALITY_ID);
		if (result.equals(BatchStatus.ERROR)) {
			dept44HealthUtility.setHealthIndicatorUnhealthy(jobName, "Could not process relocation job");
		}
	}
}
