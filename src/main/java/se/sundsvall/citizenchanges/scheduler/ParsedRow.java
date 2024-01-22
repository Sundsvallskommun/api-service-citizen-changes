package se.sundsvall.citizenchanges.scheduler;

import com.poiji.annotation.ExcelCell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class ParsedRow {

	@ExcelCell(0)
	private String personId;

	@ExcelCell(2)
	private String unit;

	@ExcelCell(3)
	private String placementStart;

	@ExcelCell(4)
	private String placementEnd;

	@ExcelCell(5)
	private String changeStart;

	@ExcelCell(6)
	private String taxCategory;

	@ExcelCell(8)
	private String calculatingFounder;

	@ExcelCell(9)
	private String guardian1;

	@ExcelCell(10)
	private String guardian2;

}
