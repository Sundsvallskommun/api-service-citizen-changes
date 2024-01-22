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

	@Schema(example = "noname@sundsvall.se")
	private String emailAddress;

	@Schema(example = "+4670111222333")
	private String phoneNumber;

	@Schema(example = "true")
	private boolean contactBySMS;

	@Schema(example = "Alpha Adminsson")
	private String displayName;

	@Schema(example = "Alpha")
	private String firstName;

	@Schema(example = "Adminsson")
	private String lastName;

}
