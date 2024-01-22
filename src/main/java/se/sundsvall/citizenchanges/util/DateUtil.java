package se.sundsvall.citizenchanges.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

	static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public static String format(final LocalDateTime localDateTime) {
		return localDateTime.format(formatter);
	}

	public static LocalDate getFromDateMeta(final LocalDate today, int backtrackDays) {
		backtrackDays = Math.min(backtrackDays, Constants.META_BACKTRACK_DAYS_MAX);
		backtrackDays = (backtrackDays < 0) ? Constants.META_BACKTRACK_DAYS_DEFAULT : backtrackDays;
		return today.minusDays(backtrackDays);
	}

	public static LocalDate getFromDateOeP(final LocalDate today) {
		//Always check from X years back.
		final int yearSpan = Constants.OEP_BACKTRACK_YEARS;
		final int thisYear = today.getYear();
		final int startYear = thisYear - yearSpan;
		return LocalDate.parse(startYear + "-01-01");
	}

	public static LocalDate getFromDate(final LocalDate today, int backtrackDays) {
		backtrackDays = (backtrackDays < 0) ? Constants.IST_BACKTRACK_DAYS_DEFAULT : backtrackDays;
		return today.minusDays(backtrackDays);
	}

}
