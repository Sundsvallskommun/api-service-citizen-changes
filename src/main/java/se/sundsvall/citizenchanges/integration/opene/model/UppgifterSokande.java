package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UppgifterSokande {

	@JacksonXmlProperty(localName = "Firstname")
	private String firstname;

	@JacksonXmlProperty(localName = "Lastname")
	private String lastname;

	@JacksonXmlProperty(localName = "SocialSecurityNumber")
	private String socialSecurityNumber;

}
