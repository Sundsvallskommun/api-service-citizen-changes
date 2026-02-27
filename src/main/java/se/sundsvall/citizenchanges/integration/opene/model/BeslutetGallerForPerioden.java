package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeslutetGallerForPerioden {

	@JacksonXmlProperty(localName = "StartDate")
	private String startDate;

	@JacksonXmlProperty(localName = "EndDate")
	private String endDate;

}
