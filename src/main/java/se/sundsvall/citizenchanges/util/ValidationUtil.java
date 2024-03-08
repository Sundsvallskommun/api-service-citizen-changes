package se.sundsvall.citizenchanges.util;


import static ch.qos.logback.core.util.OptionHelper.isNullOrEmpty;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_DECIDED;
import static se.sundsvall.citizenchanges.util.Constants.OEP_ERRAND_STATUS_READY;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

import se.sundsvall.citizenchanges.api.model.OepErrandItem;

public final class ValidationUtil {

	private ValidationUtil() {}

	public static boolean validMSISDN(final String number) {
		if (number == null) {
			return false;
		}
		final var pattern = Pattern.compile("^\\+[1-9]\\d{3,14}$");
		return pattern.matcher(number).matches();
	}

	public static boolean shouldProcessErrand(final int qualifiedItems, final int errandListSize, final int firstErrand, final int numOfErrands) {
		return qualifiedItems >= firstErrand && (errandListSize <= numOfErrands || numOfErrands == 0);
	}

	public static boolean isOepErrandQualifiedForDayCareCheck(final OepErrandItem item, final LocalDate today) {
		// Criteria for an errand to be qualified for a reminder to be sent out:
		// The application must not be denied.
		// The current date should be within the decision date span.
		// There must be a placement at a daycare center.
		// There must be a minor identifier

		if (Optional.ofNullable(item.getDecision())
			.orElse("").startsWith(Constants.OEP_ERRAND_SKOLSKJUTS_DENIED)) {
			return false;
		}

		if (isNullOrEmpty(item.getDaycarePlacement()) || isNullOrEmpty(item.getMinorIdentifier())) {
			return false;
		}

		if (isNullOrEmpty(item.getDecisionStart()) || isNullOrEmpty(item.getDecisionEnd())) {
			return false;
		}

		final LocalDate decisionStart = LocalDate.parse(item.getDecisionStart());
		final LocalDate decisionEnd = LocalDate.parse(item.getDecisionEnd());

		return (!today.isAfter(decisionEnd) && !today.isBefore(decisionStart));
	}

	public static boolean isOepErrandQualifiedForRelocationCheck(final OepErrandItem item, final LocalDate today) {
		// The application must not be denied.
		// The current date should be within the decision date span.
		final var decisionStart = item.getDecisionStart();
		final var decisionEnd = item.getDecisionEnd();



		if (Optional.ofNullable(item.getDecision()).orElse("").startsWith(Constants.OEP_ERRAND_SKOLSKJUTS_DENIED)
			|| Optional.ofNullable(item.getDecision()).orElse("").startsWith(Constants.OEP_ERRAND_ELEVRESA_DENIED)) {
			return false;
		} else if (decisionEnd != null && !decisionEnd.isEmpty() && decisionStart != null && !decisionStart.isEmpty()) {
			return (!today.isAfter(LocalDate.parse(decisionEnd)) && !today.isBefore(LocalDate.parse(decisionStart)));
		} else {
			return true;
		}
	}

	public static boolean isOEpErrandStatusValid(final String status) {
		return OEP_ERRAND_STATUS_DECIDED.equalsIgnoreCase(status) || OEP_ERRAND_STATUS_READY.equalsIgnoreCase(status);
	}

}
