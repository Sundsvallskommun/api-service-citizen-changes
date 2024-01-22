package se.sundsvall.citizenchanges.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ApplicantInfo {

	private String applicantIdentifier;

	private String applicantName;

	private String firstName;

	private String lastName;

	private boolean primaryGuardian;

}
