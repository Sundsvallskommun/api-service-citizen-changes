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

class ApplicantInfoTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(ApplicantInfo.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilder() {

		// Arrange
		final var primaryGuardian = true;
		final var firstName = "someFirstName";
		final var lastName = "someLastName";
		final var applicantName = "someApplicantName";
		final var applicantIdentifier = "someApplicantIdentifier";

		// Act
		final var result =
			ApplicantInfo.builder()
				.withPrimaryGuardian(primaryGuardian)
				.withFirstName(firstName)
				.withLastName(lastName)
				.withApplicantName(applicantName)
				.withApplicantIdentifier(applicantIdentifier)
				.build();

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getApplicantName()).isEqualTo(applicantName);
		assertThat(result.getFirstName()).isEqualTo(firstName);
		assertThat(result.getLastName()).isEqualTo(lastName);
		assertThat(result.isPrimaryGuardian()).isEqualTo(primaryGuardian);
		assertThat(result.getApplicantIdentifier()).isEqualTo(applicantIdentifier);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new ApplicantInfo()).hasAllNullFieldsOrPropertiesExcept("primaryGuardian")
			.satisfies(bean -> assertThat(bean.isPrimaryGuardian()).isFalse());
		assertThat(ApplicantInfo.builder().build()).hasAllNullFieldsOrPropertiesExcept("primaryGuardian")
			.satisfies(bean -> assertThat(bean.isPrimaryGuardian()).isFalse());
	}

}
