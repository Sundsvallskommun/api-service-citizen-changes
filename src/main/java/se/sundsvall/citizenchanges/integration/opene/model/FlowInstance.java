package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowInstance {

	private String flowInstanceID;

	@JacksonXmlProperty(localName = "Header")
	private Header header;

	@JacksonXmlProperty(localName = "Values")
	private Values values;

}
