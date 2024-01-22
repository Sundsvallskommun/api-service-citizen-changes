package se.sundsvall.citizenchanges.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.service.DaycareCheckService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Problem.class)))
@RequestMapping(value = "/daycare")
public class DayCareCheckResource {

	private final DaycareCheckService daycareCheckService;

	public DayCareCheckResource(final DaycareCheckService daycareCheckService) {
		this.daycareCheckService = daycareCheckService;
	}

	@PostMapping(value = "/batchtrigger/daycarechecker", produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BatchStatus> runDaycareCheckBatch(
		@RequestParam("firstErrand") final int firstErrand,
		@RequestParam("numOfErrands") final int numOfErrands,
		@RequestParam("backtrackDays") final int backtrackDays,
		@RequestParam("file") final MultipartFile file) throws IOException {

		final var thisResponse = daycareCheckService.runBatch(firstErrand, numOfErrands, backtrackDays, file);
		return ResponseEntity.ok(thisResponse);
	}

	@GetMapping(value = "/cachedFile", produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	@Schema(description = "Check if a cached file exists. Returns true if file exists or false if" +
		" it does not.")
	public ResponseEntity<Boolean> checkCachedFile() {
		return ResponseEntity.ok(daycareCheckService.checkCachedFile());
	}

	@DeleteMapping(value = "/cachedFile", produces = {APPLICATION_PROBLEM_JSON_VALUE})
	@Schema(description = "Try to delete cached file. If successful returns true else false." +
		" it does not.")
	public ResponseEntity<Void> deleteCachedFile() {
		daycareCheckService.deleteCachedFile();
		return ResponseEntity.ok().build();
	}

}
