package se.sundsvall.citizenchanges.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
import se.sundsvall.citizenchanges.service.ReminderService;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Problem.class)))
@RequestMapping(value = "/{municipalityId}/reminder")
public class ReminderResource {

	private final ReminderService reminderService;

	public ReminderResource(final ReminderService reminderService) {
		this.reminderService = reminderService;
	}

	@GetMapping(value = "/batchtrigger/reminder/endofterm", produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	@Operation(description = "Use this to do a live run of the batch job. Messages will be sent to the citizens if sendMessage is true.")
	public ResponseEntity<BatchStatus> runReminderBatch(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestParam("firstErrand") final int firstErrand,
		@RequestParam("numOfErrands") final int numOfErrands,
		@RequestParam("sendMessage") final boolean sendMessage) {

		final var thisResponse = reminderService.runBatch(firstErrand, numOfErrands, sendMessage, municipalityId);
		return ResponseEntity.ok(thisResponse);
	}

	@PostMapping(value = "/batchtrigger/reminder/endofterm", consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	public ResponseEntity<BatchStatus> runReminderBatch(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestBody @NotNull @Valid final List<String> oepErrands,
		@RequestParam("sendMessage") final boolean sendMessage) {
		final var thisResponse = reminderService.runBatch(0, 0, sendMessage, oepErrands, null, null, municipalityId);
		return ResponseEntity.ok(thisResponse);
	}

	@GetMapping(value = "/batchtrigger/reminder/endofterm/dryrun", produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	@Operation(description = "Use this to do a dry run of the batch job. No messages will be sent" +
		" to the citizens. Sms and email will be sent to the specified recipients.")
	public ResponseEntity<BatchStatus> runReminderBatch(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestParam("firstErrand") final int firstErrand,
		@RequestParam("numOfErrands") final int numOfErrands,
		@RequestParam("sms") @NotNull final String sms,
		@RequestParam("email") @NotNull final String email) {

		final var thisResponse = reminderService.runBatch(firstErrand, numOfErrands, sms, email, municipalityId);
		return ResponseEntity.ok(thisResponse);
	}

}
