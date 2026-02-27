package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarnetsUppgifter {

	@JacksonXmlProperty(localName = "Barnets_personummer")
	private String personummer;

	@JacksonXmlProperty(localName = "Barnets_fornamn")
	private String fornamn;

	@JacksonXmlProperty(localName = "Barnets_efternamn")
	private String efternamn;

}
