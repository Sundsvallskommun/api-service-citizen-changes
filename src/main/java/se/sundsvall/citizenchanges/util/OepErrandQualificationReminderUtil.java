package se.sundsvall.citizenchanges.util;

import java.time.LocalDate;
import java.util.Optional;

import se.sundsvall.citizenchanges.api.model.ContactInfo;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;

public final class OepErrandQualificationReminderUtil {

	private OepErrandQualificationReminderUtil() {
		// Utility class
	}

	public static boolean isOepErrandQualified(final OepErrandItem item, final LocalDate today) {
		// Criterias for an errand to be qualified for a reminder to be sent out:
		// The application must not be denied.
		// The current date should be within the decision date span.
		// The end date of the decision date span should be before the start of school in the autumn (limitDate)
		// The minor should not attend 9th grade (be 16 years old or more)
		// None of the contact person or the minor can have protected identity
		// There must be a minor identifier
		final var decisionStart = item.getDecisionStart();
		final var decisionEnd = item.getDecisionEnd();
		final var thisYear = today.getYear();
		final var nextYear = today.plusYears(1).getYear();
		final var thisMonth = today.getMonthValue();
		final var isSpring = thisMonth < 7;
		final var limitDate = isSpring ? thisYear + Constants.REMINDER_DATE_LIMIT_PATTERN_SPRING : nextYear + Constants.REMINDER_DATE_LIMIT_PATTERN_AUTUMN;
		final var lastDayOfAugust = LocalDate.parse(limitDate);
		final var minorName = Optional.ofNullable(item.getMinorName()).orElse("").toLowerCase();
		final var contactName = Optional.ofNullable(Optional.ofNullable(item.getContactInfo()).orElse(ContactInfo.builder().build()).getDisplayName()).orElse("").toLowerCase();
		final var protectedIdentityIndicator = Constants.PROTECTED_IDENTITY_INDICATOR.toLowerCase();

		if (Optional.ofNullable(item.getDecision())
			.orElse("")
			.startsWith(Constants.OEP_ERRAND_SKOLSKJUTS_DENIED)
			|| minorName.startsWith(protectedIdentityIndicator)
			|| contactName.startsWith(protectedIdentityIndicator)
			|| (item.getMinorIdentifier() == null)
			|| (item.getMinorIdentifier().trim().isEmpty())
			|| minorName.isEmpty()) {
			return false;
		} else if ((decisionEnd != null) && (!decisionEnd.isEmpty()) && (decisionStart != null) && (!decisionStart.isEmpty())) {
			final boolean valid = ((!today.isAfter(LocalDate.parse(decisionEnd))) && (!today.isBefore(LocalDate.parse(decisionStart))));
			if (!valid) {
				return false;
			} else if (!lastDayOfAugust.isBefore(LocalDate.parse(decisionEnd))) {
				var minorIdentifier = Optional.of(item.getMinorIdentifier()).orElse("");
				minorIdentifier = !minorIdentifier.isEmpty() ? minorIdentifier : String.valueOf(thisYear);
				final int birthYear = Integer.parseInt(minorIdentifier.substring(0, 4));
				final int age = thisYear - birthYear;
				return age < Constants.REMINDER_MAX_AGE;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
