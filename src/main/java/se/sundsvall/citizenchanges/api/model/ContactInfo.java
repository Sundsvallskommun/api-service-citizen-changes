package se.sundsvall.citizenchanges.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ContactInfo {

	@Schema(examples = "noname@sundsvall.se")
	private String emailAddress;

	@Schema(examples = "+4670111222333")
	private String phoneNumber;

	@Schema(examples = "true")
	private boolean contactBySMS;

	@Schema(examples = "Alpha Adminsson")
	private String displayName;

	@Schema(examples = "Alpha")
	private String firstName;

	@Schema(examples = "Adminsson")
	private String lastName;

}
