package se.sundsvall.citizenchanges.integration.opene;

import static se.sundsvall.citizenchanges.integration.opene.configuration.OpenEIntegrationConfiguration.CLIENT_ID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import se.sundsvall.citizenchanges.integration.opene.configuration.OpenEIntegrationConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.open-e.base-url}", configuration = OpenEIntegrationConfiguration.class)

public interface OpenEClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@GetMapping(path = "/api/instanceapi/getinstances/family/{familyId}", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getErrandIds(@PathVariable(name = "familyId") final String familyId,
		@RequestParam(name = "status") final String status,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);

	@GetMapping(path = "/api/instanceapi/getinstance/{flowInstanceId}/xml", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getErrand(@PathVariable(name = "flowInstanceId") String flowInstanceId);

	@GetMapping(path = "/api/instanceapi/getstatus/{flowInstanceId}", consumes = TEXT_XML_CHARSET_ISO_8859_1, produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getErrandStatus(@PathVariable(name = "flowInstanceId") String flowInstanceId);

}
