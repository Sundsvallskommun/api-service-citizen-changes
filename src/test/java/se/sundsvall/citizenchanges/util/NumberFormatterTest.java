package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.citizenchanges.util.NumberFormatter.formatMobileNumber;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class NumberFormatterTest {

	private static Pattern REG_EXP_VALID_MOBILE_NUMBER = Pattern.compile("^\\+[1-9]\\d{3,14}$");

	@Test
	void formatMobileNumberTest() {
		// Arrange
		final var testNumber1 = "070-123 12 34";
		final var testNumber2 = "075-342 21 323";
		final var testNumber3 = "abc123_ -";
		final var testNumber4 = "060-123 123";

		//Act
		final var testResult1 = formatMobileNumber(testNumber1);
		final var testResult2 = formatMobileNumber(testNumber2);
		final var testResult3 = formatMobileNumber(testNumber3);
		final var testResult4 = formatMobileNumber(testNumber4);
		final var testResult5 = formatMobileNumber(null);

		//Assert
		assertThat(REG_EXP_VALID_MOBILE_NUMBER.matcher(testResult1).matches()).isTrue();
		assertThat(REG_EXP_VALID_MOBILE_NUMBER.matcher(testResult2).matches()).isTrue();
		assertThat(REG_EXP_VALID_MOBILE_NUMBER.matcher(testResult3).matches()).isFalse();
		assertThat(REG_EXP_VALID_MOBILE_NUMBER.matcher(testResult4).matches()).isTrue();
		assertThat(REG_EXP_VALID_MOBILE_NUMBER.matcher(testResult5).matches()).isFalse();
	}

}
