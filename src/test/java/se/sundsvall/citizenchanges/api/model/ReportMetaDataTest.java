package se.sundsvall.citizenchanges.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class ReportMetaDataTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(ReportMetaData.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var reportType = "someReportType";
		final var reportTimestamp = "someReportTimestamp";
		final var metaStartDate = "someMetaStartDate";
		final var eduCloudStartDate = "someEduCloudStartDate";
		final var oepStartDate = "someOepStartDate";
		final var changedAddressesCount = 1;
		final var inspectErrandsCount = 1;

		// Act
		final var reportMetaData = ReportMetaData.builder()
			.withReportType(reportType)
			.withReportTimestamp(reportTimestamp)
			.withMetaStartDate(metaStartDate)
			.withEduCloudStartDate(eduCloudStartDate)
			.withOepStartDate(oepStartDate)
			.withChangedAddressesCount(changedAddressesCount)
			.withInspectErrandsCount(inspectErrandsCount)
			.build();

		// Assert
		assertThat(reportMetaData).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(reportMetaData.getReportType()).isEqualTo(reportType);
		assertThat(reportMetaData.getReportTimestamp()).isEqualTo(reportTimestamp);
		assertThat(reportMetaData.getMetaStartDate()).isEqualTo(metaStartDate);
		assertThat(reportMetaData.getEduCloudStartDate()).isEqualTo(eduCloudStartDate);
		assertThat(reportMetaData.getOepStartDate()).isEqualTo(oepStartDate);
		assertThat(reportMetaData.getChangedAddressesCount()).isEqualTo(changedAddressesCount);
		assertThat(reportMetaData.getInspectErrandsCount()).isEqualTo(inspectErrandsCount);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new ReportMetaData()).hasAllNullFieldsOrPropertiesExcept("changedAddressesCount", "inspectErrandsCount")
			.satisfies(bean -> {
				assertThat(bean.getChangedAddressesCount()).isZero();
				assertThat(bean.getInspectErrandsCount()).isZero();
			});
		assertThat(ReportMetaData.builder().build()).hasAllNullFieldsOrPropertiesExcept("changedAddressesCount", "inspectErrandsCount")
			.satisfies(bean -> {
				assertThat(bean.getChangedAddressesCount()).isZero();
				assertThat(bean.getInspectErrandsCount()).isZero();
			});
	}

}
