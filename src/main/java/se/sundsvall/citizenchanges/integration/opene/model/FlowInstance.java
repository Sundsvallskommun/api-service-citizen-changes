package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowInstance {

	private String flowInstanceID;

	@JacksonXmlProperty(localName = "Header")
	private Header header;

	@JacksonXmlProperty(localName = "Values")
	private Values values;

}
