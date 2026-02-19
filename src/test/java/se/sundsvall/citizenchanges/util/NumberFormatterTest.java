package se.sundsvall.citizenchanges.util;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.citizenchanges.util.NumberFormatter.formatMobileNumber;

class NumberFormatterTest {

	private static final String VALID_MOBILE_NUMBER_REG_EXP = "^\\+[1-9]\\d{3,14}$";
	private static final Pattern VALID_MOBILE_NUMBER_PATTERN = Pattern.compile(VALID_MOBILE_NUMBER_REG_EXP);

	@Test
	void formatMobileNumberTest() {
		// Arrange
		final var testNumber1 = "070-123 12 34";
		final var testNumber2 = "075-342 21 323";
		final var testNumber3 = "abc123_ -";
		final var testNumber4 = "060-123 123";

		// Act
		final var testResult1 = formatMobileNumber(testNumber1);
		final var testResult2 = formatMobileNumber(testNumber2);
		final var testResult3 = formatMobileNumber(testNumber3);
		final var testResult4 = formatMobileNumber(testNumber4);
		final var testResult5 = formatMobileNumber(null);

		// Assert
		assertThat(VALID_MOBILE_NUMBER_PATTERN.matcher(testResult1).matches()).isTrue();
		assertThat(VALID_MOBILE_NUMBER_PATTERN.matcher(testResult2).matches()).isTrue();
		assertThat(VALID_MOBILE_NUMBER_PATTERN.matcher(testResult3).matches()).isFalse();
		assertThat(VALID_MOBILE_NUMBER_PATTERN.matcher(testResult4).matches()).isTrue();
		assertThat(VALID_MOBILE_NUMBER_PATTERN.matcher(testResult5).matches()).isFalse();
	}

}
