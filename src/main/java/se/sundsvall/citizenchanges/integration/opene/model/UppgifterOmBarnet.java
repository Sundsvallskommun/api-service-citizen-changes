package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UppgifterOmBarnet {

	@JacksonXmlProperty(localName = "UppgifterOmBarnetPersonummer")
	private String personnummer;

	@JacksonXmlProperty(localName = "tfNummer")
	private String tfNummer;

	@JacksonXmlProperty(localName = "Fornamn")
	private String fornamn;

	@JacksonXmlProperty(localName = "Efternamn")
	private String efternamn;

	@JacksonXmlProperty(localName = "Barnets_namn")
	private String barnetsNamn;

}
