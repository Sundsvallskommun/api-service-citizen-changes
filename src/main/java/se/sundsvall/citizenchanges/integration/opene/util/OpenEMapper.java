package se.sundsvall.citizenchanges.integration.opene.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.sundsvall.citizenchanges.api.model.ApplicantInfo;
import se.sundsvall.citizenchanges.api.model.ContactInfo;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.integration.opene.model.Besked;
import se.sundsvall.citizenchanges.integration.opene.model.Beslut;
import se.sundsvall.citizenchanges.integration.opene.model.BeslutPeriod;
import se.sundsvall.citizenchanges.integration.opene.model.BeslutetGallerForPerioden;
import se.sundsvall.citizenchanges.integration.opene.model.DatumAndradFritidsplats;
import se.sundsvall.citizenchanges.integration.opene.model.Enhet;
import se.sundsvall.citizenchanges.integration.opene.model.Flow;
import se.sundsvall.citizenchanges.integration.opene.model.FlowInstance;
import se.sundsvall.citizenchanges.integration.opene.model.Guardian;
import se.sundsvall.citizenchanges.integration.opene.model.Guardians;
import se.sundsvall.citizenchanges.integration.opene.model.Handlaggare;
import se.sundsvall.citizenchanges.integration.opene.model.Header;
import se.sundsvall.citizenchanges.integration.opene.model.KontaktuppgifterPrivatperson;
import se.sundsvall.citizenchanges.integration.opene.model.OmFritidsplats;
import se.sundsvall.citizenchanges.integration.opene.model.OmFritidsplatsAndras;
import se.sundsvall.citizenchanges.integration.opene.model.Skola;
import se.sundsvall.citizenchanges.integration.opene.model.SkolaAnsokan;
import se.sundsvall.citizenchanges.integration.opene.model.Status;
import se.sundsvall.citizenchanges.integration.opene.model.UppgifterBarn;
import se.sundsvall.citizenchanges.integration.opene.model.UppgifterSokande;
import se.sundsvall.citizenchanges.integration.opene.model.ValdBarn;
import se.sundsvall.citizenchanges.integration.opene.model.Values;
import se.sundsvall.citizenchanges.integration.opene.model.VemGallerArendet;

import static java.util.Collections.emptyList;

@Component
public class OpenEMapper {

	private static final Logger LOG = LoggerFactory.getLogger(OpenEMapper.class);
	private final SAXParserFactory factory = SAXParserFactory.newInstance();

	public List<String> mapFlowIds(final InputStream errands) {
		try {
			SAXParser saxParser = factory.newSAXParser();
			FlowInstanceIDHandler handler = new FlowInstanceIDHandler();
			saxParser.parse(errands, handler);

			return handler.getFlowInstanceIDs();
		} catch (final Exception e) {
			LOG.info("Something went wrong parsing flowInstances", e);
			return emptyList();
		}
	}

	public OepErrandItem mapFlowInstance(final byte[] errand, final FamilyType familyType) {
		final var xmlString = new String(errand, StandardCharsets.ISO_8859_1).replaceAll("<EncodedData>.*</EncodedData>", "removed");
		final FlowInstance flowInstance;
		final var item = OepErrandItem.builder();
		try {
			flowInstance = new XmlMapper()
				.registerModule(new StringTrimModule())
				.readValue(xmlString, FlowInstance.class);
		} catch (final Exception e) {
			LOG.info("Something went wrong parsing flowInstance", e);
			return new OepErrandItem();
		}

		final var header = Optional.ofNullable(flowInstance.getHeader()).orElse(new Header());
		final var values = flowInstance.getValues();

		if (values == null) {
			return item.build();
		}
		item.withFlowInstanceId(header.getFlowInstanceID())
			.withFamilyId(Optional.ofNullable(header.getFlow()).orElse(new Flow()).getFamilyID())
			.withStatus(Optional.ofNullable(header.getStatus()).orElse(new Status()).getName())
			.withApplicantCapacity(Optional.ofNullable(values.getVemGallerArendet()).orElse(new VemGallerArendet()).getValue());

		if (familyType.equals(FamilyType.SKOLSKJUTS)) {
			final var contactInfo = mapContactInfo(values.getKontaktuppgifterPrivatperson());

			item.withContactInfo(contactInfo)
				.withAdministratorName(Optional.ofNullable(values.getHandlaggare()).orElse(new Handlaggare()).getValue())
				.withSchool(Optional.ofNullable(values.getSkolaAnsokan()).orElse(new SkolaAnsokan()).getValue())
				.withDecisionStart(Optional.ofNullable(values.getBeslutPeriod()).orElse(new BeslutPeriod()).getStartDate())
				.withDecisionEnd(Optional.ofNullable(values.getBeslutPeriod()).orElse(new BeslutPeriod()).getEndDate())
				.withDecision(Optional.ofNullable(values.getBeslut()).orElse(new Beslut()).getValue())
				.withDaycarePlacement(Optional.ofNullable(values.getOmFritidsplats()).orElse(new OmFritidsplats()).getValue())
				.withDaycarePlacementChanged(Optional.ofNullable(values.getOmFritidsplatsAndras()).orElse(new OmFritidsplatsAndras()).getValue())
				.withDaycarePlacementChangedFrom(Optional.ofNullable(values.getDatumAndradFritidsplats()).orElse(new DatumAndradFritidsplats()).getStartDate())
				.withApplicants(mapApplicantInfo(contactInfo, Optional.ofNullable(Optional.ofNullable(values.getValdBarn()).orElse(new ValdBarn()).getGuardians()).orElse(new Guardians()).getGuardian()));

			mapMinorIdentifierAndName(item, values);
			return item.build();

		} else {
			return item.withApplicants(mapApplicantInfo(values.getUppgifterSokande()))
				.withSchool(Optional.ofNullable(values.getSkola()).orElse(new Skola()).getValue())
				.withSchoolUnit(Optional.ofNullable(values.getEnhet()).orElse(new Enhet()).getValue())
				.withMinorIdentifier(Optional.ofNullable(values.getUppgifterBarn()).orElse(new UppgifterBarn()).getCitizenIdentifier())
				.withMinorName(Optional.ofNullable(values.getUppgifterBarn()).orElse(new UppgifterBarn()).getFirstname() + " " + Optional.ofNullable(values.getUppgifterBarn()).orElse(new UppgifterBarn()).getLastname())
				.withDecisionStart(Optional.ofNullable(values.getBeslutetGallerForPerioden()).orElse(new BeslutetGallerForPerioden()).getStartDate())
				.withDecisionEnd(Optional.ofNullable(values.getBeslutetGallerForPerioden()).orElse(new BeslutetGallerForPerioden()).getEndDate())
				.withDecision(Optional.ofNullable(values.getBesked()).orElse(new Besked()).getValue())
				.build();
		}
	}

	void mapMinorIdentifierAndName(final OepErrandItem.OepErrandItemBuilder<?, ? extends OepErrandItem.OepErrandItemBuilder<?, ?>> item, final Values values) {
		if (values.getValdBarn() != null) {
			item.withMinorIdentifier(values.getValdBarn().getCitizenIdentifier())
				.withMinorName(values.getValdBarn().getFirstname() + " " + values.getValdBarn().getLastname());

		} else if (values.getUppgifterOmBarnet() != null) {

			if (values.getUppgifterOmBarnet().getPersonnummer() != null) {
				item.withMinorIdentifier(values.getUppgifterOmBarnet().getPersonnummer());
			} else {
				item.withMinorIdentifier(values.getUppgifterOmBarnet().getTfNummer());
			}

			if (values.getUppgifterOmBarnet().getFornamn() != null && values.getUppgifterOmBarnet().getEfternamn() != null) {
				item.withMinorName(values.getUppgifterOmBarnet().getFornamn() + " " + values.getUppgifterOmBarnet().getEfternamn());
			} else {
				item.withMinorName(values.getUppgifterOmBarnet().getBarnetsNamn());
			}
		} else if (values.getBarnetsUppgifter() != null) {
			item.withMinorIdentifier(values.getBarnetsUppgifter().getPersonummer())
				.withMinorName(values.getBarnetsUppgifter().getFornamn() + " " + values.getBarnetsUppgifter().getEfternamn());
		}
	}

	private ContactInfo mapContactInfo(final KontaktuppgifterPrivatperson kontaktuppgifter) {
		if (kontaktuppgifter == null) {
			return ContactInfo.builder().build();
		}
		return ContactInfo.builder()
			.withPhoneNumber(kontaktuppgifter.getMobilePhone())
			.withEmailAddress(kontaktuppgifter.getEmail())
			.withDisplayName(kontaktuppgifter.getFirstname() + " " + kontaktuppgifter.getLastname())
			.withFirstName(kontaktuppgifter.getFirstname())
			.withLastName(kontaktuppgifter.getLastname())
			.withContactBySMS(kontaktuppgifter.isContactBySMS())
			.build();
	}

	private List<ApplicantInfo> mapApplicantInfo(final ContactInfo contactInfo, final List<Guardian> guardians) {
		if (guardians == null) {
			return emptyList();
		}
		return guardians.stream()
			.map(guardian -> ApplicantInfo.builder()
				.withApplicantName(guardian.getFirstname() + " " + guardian.getLastname())
				.withFirstName(guardian.getFirstname())
				.withLastName(guardian.getLastname())
				.withApplicantIdentifier(guardian.getCitizenIdentifier())
				.withPrimaryGuardian(isApplicant(contactInfo, guardian))
				.build())
			.toList();
	}

	private List<ApplicantInfo> mapApplicantInfo(final List<UppgifterSokande> uppgifterSokandeList) {
		if (uppgifterSokandeList == null) {
			return emptyList();
		}
		return uppgifterSokandeList.stream()
			.map(uppgifterSokande -> ApplicantInfo.builder()
				.withApplicantName(uppgifterSokande.getFirstname() + " " + uppgifterSokande.getLastname())
				.withFirstName(uppgifterSokande.getFirstname())
				.withLastName(uppgifterSokande.getLastname())
				.withApplicantIdentifier(uppgifterSokande.getSocialSecurityNumber())
				.build())
			.toList();
	}

	private boolean isApplicant(final ContactInfo contact, final Guardian guardian) {

		return Optional.ofNullable(guardian.getFirstname()).orElse("").equalsIgnoreCase(contact.getFirstName())
			&& Optional.ofNullable(guardian.getLastname()).orElse("").equalsIgnoreCase(contact.getLastName());
	}

	public String mapStatus(final byte[] errandStatus) {

		final var xmlString = new String(errandStatus);
		try {
			return new XmlMapper()
				.registerModule(new StringTrimModule())
				.readValue(xmlString, Status.class)
				.getName();
		} catch (final Exception e) {
			LOG.info("Something went wrong parsing flowInstances", e);
			return null;
		}

	}

}
