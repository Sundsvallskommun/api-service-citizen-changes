package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

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
