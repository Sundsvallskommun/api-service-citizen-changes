package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValdBarn {

	@JacksonXmlProperty(localName = "CitizenIdentifier")
	private String citizenIdentifier;

	@JacksonXmlProperty(localName = "Firstname")
	private String firstname;

	@JacksonXmlProperty(localName = "Lastname")
	private String lastname;

	@JacksonXmlProperty(localName = "Guardians")
	private Guardians guardians;

}
