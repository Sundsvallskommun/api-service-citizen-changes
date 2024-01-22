package se.sundsvall.citizenchanges.integration.citizen;

import static se.sundsvall.citizenchanges.integration.citizen.configuration.CitizenIntegrationConfiguration.CLIENT_ID;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.sundsvall.citizenchanges.integration.citizen.configuration.CitizenIntegrationConfiguration;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.citizen.base-url}",
	configuration = CitizenIntegrationConfiguration.class)
public interface CitizenClient {

	@GetMapping("/changedaddress")
	Set<CitizenWithChangedAddress> getAddressChanges(@RequestParam("changedDateFrom") String changedDateFrom);

}
