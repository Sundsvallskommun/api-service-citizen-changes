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

	@Schema(example = "1699")
	private String flowInstanceId;

	@Schema(example = "344")
	private String familyId;

	@Schema(example = "2021-01-01")
	private String decisionStart;

	@Schema(example = "2021-12-31")
	private String decisionEnd;

	private AddressItem newAddress;

	private AddressItem oldAddress;

	@Schema(example = "Test Testorsson")
	private String nameGuardian;

	@Schema(example = "Test Testorsson")
	private String nameMinor;

	@Schema(example = "Utflyttad")
	private String status;

	@Schema(example = "Nej")
	private String classified;

	@Schema(example = "Admin Adminsson")
	private String administratorName;

	@Schema(example = "Sundsvalls gymnasium Hedbergska")
	private String schoolUnit;

	@Schema(example = "Ja")
	private String minorToSameAddress;

	@Schema(example = "VH,Elev")
	private String moverList;

	@Schema(example = "Introduktionsprogrammet, Västermalm")
	private String schoolProgram;

}
