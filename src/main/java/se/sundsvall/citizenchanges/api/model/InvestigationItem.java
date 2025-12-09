package se.sundsvall.citizenchanges.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class InvestigationItem {

	@Schema(examples = "1699")
	private String flowInstanceId;

	@Schema(examples = "344")
	private String familyId;

	@Schema(examples = "2021-01-01")
	private String decisionStart;

	@Schema(examples = "2021-12-31")
	private String decisionEnd;

	private AddressItem newAddress;

	private AddressItem oldAddress;

	@Schema(examples = "Test Testorsson")
	private String nameGuardian;

	@Schema(examples = "Test Testorsson")
	private String nameMinor;

	@Schema(examples = "Utflyttad")
	private String status;

	@Schema(examples = "Nej")
	private String classified;

	@Schema(examples = "Admin Adminsson")
	private String administratorName;

	@Schema(examples = "Sundsvalls gymnasium Hedbergska")
	private String schoolUnit;

	@Schema(examples = "Ja")
	private String minorToSameAddress;

	@Schema(examples = "VH,Elev")
	private String moverList;

	@Schema(examples = "Introduktionsprogrammet, Västermalm")
	private String schoolProgram;

}
