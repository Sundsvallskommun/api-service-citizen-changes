package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void format() {
		// Arrange
		final var now = LocalDateTime.of(2022, 1, 1, 1, 1, 1);
		// Act
		final var result = DateUtil.format(now);
		// Assert
		assertThat(result).isEqualTo("2022-01-01 01:01");
	}

	@Test
	void getFromDateMeta() {
		// Act
		final var result = DateUtil.getFromDateMeta(LocalDate.now(), 3);
		// Assert
		assertThat(result).isEqualTo(LocalDate.now().minusDays(3));
	}

	@Test
	void getFromDateOeP() {
		// Act
		final var result = DateUtil.getFromDateOeP(LocalDate.now());
		// Assert
		assertThat(result).isEqualTo(LocalDate.now().withMonth(1).withDayOfMonth(1).minusYears(4));
	}

	@Test
	void getFromDate() {
		// Act
		final var result = DateUtil.getFromDate(LocalDate.now(), 3);
		// Assert
		assertThat(result).isEqualTo(LocalDate.now().minusDays(3));
	}

}
