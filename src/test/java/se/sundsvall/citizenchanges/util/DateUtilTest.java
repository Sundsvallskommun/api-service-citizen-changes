package se.sundsvall.citizenchanges.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
		assertThat(result).isEqualTo(LocalDate.now().withMonth(1).withDayOfMonth(1).minusYears(3));
	}

	@Test
	void getFromDate() {
		// Act
		final var result = DateUtil.getFromDate(LocalDate.now(), 3);
		// Assert
		assertThat(result).isEqualTo(LocalDate.now().minusDays(3));
	}

	@ParameterizedTest
	@MethodSource("isSpringArgumentsProvider")
	void isSpring(LocalDate localDateToTest, boolean expectedResult) {
		try (MockedStatic<LocalDate> topDateTimeUtilMock = Mockito.mockStatic(LocalDate.class)) {
			topDateTimeUtilMock.when(LocalDate::now).thenReturn(localDateToTest);
			assertThat(DateUtil.isSpring()).isEqualTo(expectedResult);
		}
	}

	private static Stream<Arguments> isSpringArgumentsProvider() {
		return Stream.of(
			Arguments.of(LocalDate.of(2024, 1, 1), true),
			Arguments.of(LocalDate.of(2024, 6, 30), true),
			Arguments.of(LocalDate.of(2024, 7, 1), false),
			Arguments.of(LocalDate.of(2024, 12, 31), false));
	}

	@Test
	void getCurrentYear() {
		assertThat(DateUtil.getCurrentYear()).isEqualTo(LocalDate.now().getYear());
	}

	@Test
	void getNextYear() {
		assertThat(DateUtil.getNextYear()).isEqualTo(LocalDate.now().plusYears(1).getYear());
	}
}
