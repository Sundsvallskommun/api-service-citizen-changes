package se.sundsvall.citizenchanges.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class AddressItem {

	@Schema(examples = "Current")
	private String status;

	@Schema(examples = "2021-11-01T00:00:00")
	private OffsetDateTime nrDate;

	private String co;

	@Schema(examples = "Testvägen 18")
	private String address;

	@Schema(examples = "LGH 1001")
	private String apartmentNumber;

	@Schema(examples = "123 45")
	private String postalCode;

	@Schema(examples = "Sundsvall")
	private String city;

	@Schema(examples = "22")
	private String county;

	@Schema(examples = "81")
	private String municipality;

	@Schema(examples = "POPULATION_REGISTRATION_ADDRESS")
	private String addressType;

	@Schema(examples = "6915726.967")
	private Double coordinateX;

	@Schema(examples = "156025.571")
	private Double coordinateY;

	@Override
	public String toString() {
		return "AddressItem{" +
			"status='" + status + '\'' +
			", nrDate='" + nrDate + '\'' +
			", co='" + co + '\'' +
			", address='" + address + '\'' +
			", apartmentNumber='" + apartmentNumber + '\'' +
			", postalCode='" + postalCode + '\'' +
			", city='" + city + '\'' +
			", county=" + county +
			", municipality=" + municipality +
			", addressType='" + addressType + '\'' +
			", coordinateX='" + coordinateX + '\'' +
			", coordinateY='" + coordinateY + '\'' +
			'}';
	}

}
