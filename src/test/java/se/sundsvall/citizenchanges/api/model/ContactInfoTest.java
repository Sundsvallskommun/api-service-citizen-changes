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

class ContactInfoTest {


	@Test
	void testBean() {
		MatcherAssert.assertThat(ContactInfo.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}


	@Test
	void testBuilderMethods() {
		// Arrange
		final var contactBySMS = true;
		final var emailAddress = "someEmailAdress";
		final var firstName = "someFirstName";
		final var lastName = "someLastName";
		final var displayName = "someDisplayName";
		final var phoneNumber = "somePhoneNumber";

		// Act
		final var contactInfo = ContactInfo.builder()
			.withContactBySMS(contactBySMS)
			.withEmailAddress(emailAddress)
			.withFirstName(firstName)
			.withLastName(lastName)
			.withDisplayName(displayName)
			.withPhoneNumber(phoneNumber)
			.build();

		// Assert
		assertThat(contactInfo).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(contactInfo.isContactBySMS()).isEqualTo(contactBySMS);
		assertThat(contactInfo.getEmailAddress()).isEqualTo(emailAddress);
		assertThat(contactInfo.getFirstName()).isEqualTo(firstName);
		assertThat(contactInfo.getLastName()).isEqualTo(lastName);
		assertThat(contactInfo.getDisplayName()).isEqualTo(displayName);
		assertThat(contactInfo.getPhoneNumber()).isEqualTo(phoneNumber);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new ContactInfo()).hasAllNullFieldsOrPropertiesExcept("contactBySMS").satisfies(
			bean -> assertThat(bean.isContactBySMS()).isFalse());
		assertThat(ContactInfo.builder().build()).hasAllNullFieldsOrPropertiesExcept("contactBySMS").satisfies(
			bean -> assertThat(bean.isContactBySMS()).isFalse());
	}

}
