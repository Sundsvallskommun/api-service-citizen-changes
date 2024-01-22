package se.sundsvall.citizenchanges.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ReportMetaData {

	private String reportType;

	private String reportTimestamp;

	private String metaStartDate;

	private String oepStartDate;

	private String eduCloudStartDate;

	private int changedAddressesCount;

	private int inspectErrandsCount;

}
