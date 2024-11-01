package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeslutetGallerForPerioden {

	@JacksonXmlProperty(localName = "StartDate")
	private String startDate;

	@JacksonXmlProperty(localName = "EndDate")
	private String endDate;

}
