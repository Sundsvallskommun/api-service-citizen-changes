package se.sundsvall.citizenchanges.integration.citizen;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.citizenchanges.integration.citizen.configuration.CitizenIntegrationConfiguration;

import static se.sundsvall.citizenchanges.integration.citizen.configuration.CitizenIntegrationConfiguration.CLIENT_ID;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.citizen.base-url}",
	configuration = CitizenIntegrationConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface CitizenClient {

	@GetMapping("/{municipalityId}/changedaddress")
	Set<CitizenWithChangedAddress> getAddressChanges(
		@PathVariable("municipalityId") String municipalityId,
		@RequestParam("changedDateFrom") String changedDateFrom);
}
