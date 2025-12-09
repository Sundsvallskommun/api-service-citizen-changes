package se.sundsvall.citizenchanges.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(setterPrefix = "with")
public class OepErrandItem {

	@Schema(examples = "1699")
	private String flowInstanceId;

	@Schema(examples = "Beslutad")
	private String status;

	private List<ApplicantInfo> applicants;

	@Schema(examples = "197408233333")
	private String minorIdentifier;

	@Schema(examples = "Minor Besson")
	private String minorName;

	@Schema(examples = "2021-01-01")
	private String decisionStart;

	@Schema(examples = "2021-12-31")
	private String decisionEnd;

	@Schema(examples = "344")
	private String familyId;

	@Schema(examples = "Avslås")
	private String decision;

	@Schema(examples = "Admin Adminsson")

	private String administratorName;

	@Schema(examples = "Sundsvalls gymnasium Hedbergska")
	private String school;

	@Schema(examples = "Introduktionsprogrammet, Västermalm")
	private String schoolUnit;

	private ContactInfo contactInfo;

	@Schema(examples = "Skickat")
	private String emailStatus;

	@Schema(examples = "Fel")
	private String smsStatus;

	@Schema(examples = "Jag ansöker för ett barn som jag är vårdnadshavare för")
	private String applicantCapacity;

	@Schema(examples = "Ja, deltid|Ja, heltid|Nej")
	private String daycarePlacement;

	@Schema(examples = "Ja, fritidsplatsen är ändrad till deltid|Ja, fritidsplatsen är uppsagd")
	private String daycarePlacementChanged;

	@Schema(examples = "2022-08-24")
	private String daycarePlacementChangedFrom;

}
