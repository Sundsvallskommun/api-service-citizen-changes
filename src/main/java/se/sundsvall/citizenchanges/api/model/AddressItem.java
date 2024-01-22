package se.sundsvall.citizenchanges.api.model;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class AddressItem {

	@Schema(example = "Current")
	private String status;

	@Schema(example = "2021-11-01T00:00:00")
	private OffsetDateTime nrDate;

	private String co;

	@Schema(example = "Testvägen 18")
	private String address;

	@Schema(example = "LGH 1001")
	private String apartmentNumber;

	@Schema(example = "123 45")
	private String postalCode;

	@Schema(example = "Sundsvall")
	private String city;

	@Schema(example = "22")
	private String county;

	@Schema(example = "81")
	private String municipality;

	@Schema(example = "POPULATION_REGISTRATION_ADDRESS")
	private String addressType;

	@Schema(example = "6915726.967")
	private Double coordinateX;

	@Schema(example = "156025.571")
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
