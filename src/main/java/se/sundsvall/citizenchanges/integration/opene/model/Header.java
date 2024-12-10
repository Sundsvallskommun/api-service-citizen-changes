package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {

	@JacksonXmlProperty(localName = "Flow")
	private Flow flow;

	@JacksonXmlProperty(localName = "Status")
	private Status status;

	@JacksonXmlProperty(localName = "FlowInstanceID")
	private String flowInstanceID;

}
