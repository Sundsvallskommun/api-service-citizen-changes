package se.sundsvall.citizenchanges.integration.opene;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.integration.opene.util.OpenEMapper;

@Component
public class OpenEIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(OpenEIntegration.class);

	private final OpenEClient client;

	private final OpenEMapper mapper;

	public OpenEIntegration(final OpenEClient client, final OpenEMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	public List<String> getErrandIds(final String familyId, final String status, final String fromDate,
		final String toDate) {

		try {
			return mapper.mapFlowIds(client.getErrandIds(familyId, status, fromDate, toDate).getInputStream());
		} catch (final Exception e) {
			LOG.info("Unable to get errandIds for familyId {}", familyId, e);
			return List.of();
		}
	}

	public OepErrandItem getErrand(final String flowInstanceId, final FamilyType familyType) {
		try {
			return mapper.mapFlowInstance(client.getErrand(flowInstanceId), familyType);
		} catch (final Exception e) {
			LOG.info("Unable to get errand for flowInstanceId {}", flowInstanceId, e);
			return new OepErrandItem();
		}

	}

}
