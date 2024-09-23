package se.sundsvall.citizenchanges.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

	private static final int JULY = 7;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private DateUtil() {
		// Intentionally left empty
	}

	public static String format(final LocalDateTime localDateTime) {
		return localDateTime.format(FORMATTER);
	}

	public static LocalDate getFromDateMeta(final LocalDate today, int backtrackDays) {
		backtrackDays = Math.min(backtrackDays, Constants.META_BACKTRACK_DAYS_MAX);
		backtrackDays = (backtrackDays < 0) ? Constants.META_BACKTRACK_DAYS_DEFAULT : backtrackDays;
		return today.minusDays(backtrackDays);
	}

	public static LocalDate getFromDateOeP(final LocalDate today) {
		// Always check from X years back.
		final var yearSpan = Constants.OEP_BACKTRACK_YEARS;
		final var thisYear = today.getYear();
		final var startYear = thisYear - yearSpan;
		return LocalDate.parse(startYear + "-01-01");
	}

	public static LocalDate getFromDate(final LocalDate today, int backtrackDays) {
		backtrackDays = (backtrackDays < 0) ? Constants.IST_BACKTRACK_DAYS_DEFAULT : backtrackDays;
		return today.minusDays(backtrackDays);
	}

	public static boolean isSpring() {
		return LocalDate.now().getMonthValue() < JULY;
	}

	public static int getCurrentYear() {
		return LocalDate.now().getYear();
	}

	public static int getNextYear() {
		return LocalDate.now().plusYears(1).getYear();
	}

}
