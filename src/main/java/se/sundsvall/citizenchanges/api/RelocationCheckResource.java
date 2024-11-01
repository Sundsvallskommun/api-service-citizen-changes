package se.sundsvall.citizenchanges.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.citizenchanges.api.model.BatchStatus;
import se.sundsvall.citizenchanges.integration.citizen.CitizenIntegration;
import se.sundsvall.citizenchanges.service.RelocationCheckService;
import se.sundsvall.citizenchanges.util.DateUtil;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;

import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Problem.class)))
@RequestMapping(value = "/{municipalityId}/relocations", produces = {
	APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
})
public class RelocationCheckResource {

	private final RelocationCheckService relocationCheckService;

	private final CitizenIntegration citizenIntegration;

	public RelocationCheckResource(final RelocationCheckService relocationCheckService, final CitizenIntegration citizenIntegration) {
		this.relocationCheckService = relocationCheckService;
		this.citizenIntegration = citizenIntegration;
	}

	@GetMapping(value = "/batchtrigger/relocations")
	public ResponseEntity<BatchStatus> runBatch(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestParam("backtrackDays") final Integer backtrackDays) {

		return ResponseEntity.ok(relocationCheckService.runBatch(Optional.of(backtrackDays), null, municipalityId));
	}

	@PostMapping(value = "/batchtrigger/relocations", consumes = APPLICATION_JSON_VALUE)
	@Operation(description = "Use this if you want to send a set of Citizeninfo to the batch job " +
		"and bypass the citizen api. ")
	public ResponseEntity<BatchStatus> runBatch(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestParam("backtrackDays") final Integer backtrackDays,
		@RequestBody final Set<CitizenWithChangedAddress> citizenWithChangedAddresses) {

		return ResponseEntity.ok(relocationCheckService.runBatch(Optional.of(backtrackDays), citizenWithChangedAddresses, municipalityId));
	}

	@GetMapping(value = "/meta/recentmoves")
	public ResponseEntity<Set<CitizenWithChangedAddress>> getRecentMoves(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestParam("backtrackDays") final Integer backtrackDays) {

		final var fromDate = DateUtil.getFromDateMeta(LocalDate.now(), backtrackDays);

		return ResponseEntity.ok(citizenIntegration.getAddressChanges(municipalityId, fromDate.toString()));
	}

}
