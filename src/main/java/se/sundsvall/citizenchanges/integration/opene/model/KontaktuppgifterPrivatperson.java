package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KontaktuppgifterPrivatperson {

	@JacksonXmlProperty(localName = "Firstname")
	private String firstname;

	@JacksonXmlProperty(localName = "Lastname")
	private String lastname;

	@JacksonXmlProperty(localName = "Email")
	private String email;

	@JacksonXmlProperty(localName = "MobilePhone")
	private String mobilePhone;

	@JacksonXmlProperty(localName = "ContactBySMS")
	private boolean contactBySMS;

}
