package se.sundsvall.citizenchanges.integration.citizen;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;

@Component
public class CitizenIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(CitizenIntegration.class);

	private final CitizenClient client;

	public CitizenIntegration(final CitizenClient client) {
		this.client = client;
	}

	public Set<CitizenWithChangedAddress> getAddressChanges(final String changedDateFrom) {
		try {
			return client.getAddressChanges(changedDateFrom);
		} catch (final Exception e) {
			LOG.info("Unable to get address changes", e);
			return Set.of();
		}
	}

}
