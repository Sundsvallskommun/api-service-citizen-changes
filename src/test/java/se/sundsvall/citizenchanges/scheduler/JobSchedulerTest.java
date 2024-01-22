package se.sundsvall.citizenchanges.scheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.service.RelocationCheckService;

@ExtendWith(MockitoExtension.class)
class JobSchedulerTest {

	@Mock
	private RelocationCheckService relocationCheckService;

	@InjectMocks
	private JobScheduler jobScheduler;

	@Test
	void startRelocationCheck() {
		// Mock
		when(relocationCheckService.runBatch()).thenReturn(BatchStatus.DONE);
		// Act
		jobScheduler.startRelocationCheck();
		// Verify
		verify(relocationCheckService).runBatch();
	}

}
