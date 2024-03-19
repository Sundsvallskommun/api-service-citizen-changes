package se.sundsvall.citizenchanges.integration.opene;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_DECIDED;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;

import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.integration.opene.util.OpenEMapper;

@ExtendWith(MockitoExtension.class)
class OpenEIntegrationTests {

	@Mock
	private OpenEClient client;

	@Mock
	private OpenEMapper mapper;

	@InjectMocks
	private OpenEIntegration openEIntegration;

	@Test
	void getErrandIds() {
		// Arrange
		when(client.getErrandIds(any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn(new byte[0]);
		when(mapper.mapFlowIds(any())).thenReturn(List.of("SomeFlowId1", "SomeFlowId2"));

		// Act
		final var result = openEIntegration.getErrandIds("any", "any", "any", "any");

		// Assert
		assertThat(result).hasSize(2);
		assertThat(result.getFirst()).isEqualTo("SomeFlowId1");
		// Verify
		verify(client, times(1)).getErrandIds(any(String.class), any(String.class), any(String.class), any(String.class));
		verify(mapper, times(1)).mapFlowIds(any());
		verifyNoMoreInteractions(client);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	void getErrandIds_error() {
		// Arrange
		when(client.getErrandIds(any(String.class), any(String.class), any(String.class), any(String.class)))
			.thenThrow(Problem.builder().build());

		// Act
		final var result = openEIntegration.getErrandIds("any", "any", "any", "any");

		// Assert & Verify
		assertThat(result).isEmpty();
		verify(client, times(1)).getErrandIds(any(String.class), any(String.class), any(String.class), any(String.class));
		verifyNoMoreInteractions(client);
		verifyNoInteractions(mapper);
	}

	@Test
	void getErrand() {
		// Arrange
		when(client.getErrand(any(String.class))).thenReturn(new byte[0]);
		when(mapper.mapFlowInstance(any(), any(FamilyType.class))).thenReturn(new OepErrandItem());

		// Act
		final var result = openEIntegration.getErrand("any", FamilyType.SKOLSKJUTS);

		// Assert & Verify
		assertThat(result).isNotNull();
		verify(client, times(1)).getErrand(any(String.class));
		verify(mapper, times(1)).mapFlowInstance(any(), any(FamilyType.class));
		verifyNoMoreInteractions(client);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	void getErrand_error() {
		// Arrange
		when(client.getErrand(any(String.class))).thenThrow(Problem.builder().build());

		// Act
		final var result = openEIntegration.getErrand("any", FamilyType.ELEVRESA);

		// Assert & Verify
		assertThat(result).isNotNull();
		verify(client, times(1)).getErrand(any(String.class));
		verifyNoMoreInteractions(client);
		verifyNoInteractions(mapper);
	}

}
