package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
