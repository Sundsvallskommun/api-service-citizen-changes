package se.sundsvall.citizenchanges.api.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(setterPrefix = "with")
public class OepErrandItem {

	@Schema(example = "1699")
	private String flowInstanceId;

	@Schema(example = "Beslutad")
	private String status;

	private List<ApplicantInfo> applicants;

	@Schema(example = "197408233333")
	private String minorIdentifier;

	@Schema(example = "Minor Besson")
	private String minorName;

	@Schema(example = "2021-01-01")
	private String decisionStart;

	@Schema(example = "2021-12-31")
	private String decisionEnd;

	@Schema(example = "344")
	private String familyId;

	@Schema(example = "Avslås")
	private String decision;

	@Schema(example = "Admin Adminsson")

	private String administratorName;

	@Schema(example = "Sundsvalls gymnasium Hedbergska")
	private String school;

	@Schema(example = "Introduktionsprogrammet, Västermalm")
	private String schoolUnit;

	private ContactInfo contactInfo;

	@Schema(example = "Skickat")
	private String emailStatus;

	@Schema(example = "Fel")
	private String smsStatus;

	@Schema(example = "Jag ansöker för ett barn som jag är vårdnadshavare för")
	private String applicantCapacity;

	@Schema(example = "Ja, deltid|Ja, heltid|Nej")
	private String daycarePlacement;

	@Schema(example = "Ja, fritidsplatsen är ändrad till deltid|Ja, fritidsplatsen är uppsagd")
	private String daycarePlacementChanged;

	@Schema(example = "2022-08-24")
	private String daycarePlacementChangedFrom;

}
