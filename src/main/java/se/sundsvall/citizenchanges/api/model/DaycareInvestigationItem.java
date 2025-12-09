package se.sundsvall.citizenchanges.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "with")
public class DaycareInvestigationItem extends OepErrandItem {

	@Schema(examples = "2021-01-01")
	private String istChangeStartDate;

	@Schema(examples = "2023-06-14")
	private String istPlacementEndDate;

	@Schema(examples = "S:t Olofsskolans fritidshem")
	private String istPlacementName;

	private String istPlacement;

}
