package se.sundsvall.citizenchanges.integration.opene.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Values {

	@JacksonXmlProperty(localName = "Beslut")
	private Beslut beslut;

	@JacksonXmlProperty(localName = "BeslutPeriod")
	private BeslutPeriod beslutPeriod;

	@JacksonXmlProperty(localName = "Handlaggare")
	private Handlaggare handlaggare;

	@JacksonXmlProperty(localName = "KontaktuppgifterPrivatperson")
	private KontaktuppgifterPrivatperson kontaktuppgifterPrivatperson;

	@JacksonXmlProperty(localName = "VemGallerArendet")
	private VemGallerArendet vemGallerArendet;

	@JacksonXmlProperty(localName = "ValdBarn")
	private ValdBarn valdBarn;

	@JacksonXmlProperty(localName = "UppgifterOmBarnet")
	private UppgifterOmBarnet uppgifterOmBarnet;

	@JacksonXmlProperty(localName = "BarnetsUppgifter")
	private BarnetsUppgifter barnetsUppgifter;

	@JacksonXmlProperty(localName = "SkolaAnsokan")
	private SkolaAnsokan skolaAnsokan;

	@JacksonXmlProperty(localName = "OmFritidsplats")
	private OmFritidsplats omFritidsplats;

	@JacksonXmlProperty(localName = "OmFritidsplatsAndras")
	private OmFritidsplatsAndras omFritidsplatsAndras;

	@JacksonXmlProperty(localName = "DatumAndradFritidsplats")
	private DatumAndradFritidsplats datumAndradFritidsplats;

	@JacksonXmlProperty(localName = "Besked")
	private Besked besked;

	@JacksonXmlProperty(localName = "UppgifterBarn")
	private UppgifterBarn uppgifterBarn;

	@JacksonXmlProperty(localName = "UppgifterSokande")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<UppgifterSokande> uppgifterSokande;

	@JacksonXmlProperty(localName = "Skola")
	private Skola skola;

	@JacksonXmlProperty(localName = "Enhet")
	private Enhet enhet;

	@JacksonXmlProperty(localName = "BeslutetGallerForPerioden")
	private BeslutetGallerForPerioden beslutetGallerForPerioden;

}
