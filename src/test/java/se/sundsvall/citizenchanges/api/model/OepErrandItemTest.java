package se.sundsvall.citizenchanges.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class OepErrandItemTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(OepErrandItem.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		
		// Arrange
		final var contactInfo = ContactInfo.builder().build();
		final var flowInstanceId = "someFlowInstanceId";
		final var status = "someStatus";
		final var minorIdentifier = "someMinorIdentifier";
		final var minorName = "someMinorName";
		final var decisionStart = "someDecisionStart";
		final var decisionEnd = "someDecisionEnd";
		final var familyId = "someFamilyId";
		final var decision = "someDecision";
		final var administratorName = "someAdministratorName";
		final var applicants = List.of(ApplicantInfo.builder().build());
		final var school = "someSchool";
		final var schoolUnit = "someSchoolUnit";
		final var emailStatus = "someEmailStatus";
		final var smsStatus = "someSmsStatus";
		final var applicantCapacity = "someApplicantCapacity";
		final var daycarePlacement = "someDaycarePlacement";
		final var daycarePlacementChanged = "someDaycarePlacementChanged";
		final var daycarePlacementChangedFrom = "someDaycarePlacementChangedFrom";

		// Act
		final var oepErrandItem = OepErrandItem.builder()
			.withFlowInstanceId(flowInstanceId)
			.withStatus(status)
			.withMinorIdentifier(minorIdentifier)
			.withMinorName(minorName)
			.withDecisionStart(decisionStart)
			.withDecisionEnd(decisionEnd)
			.withFamilyId(familyId)
			.withDecision(decision)
			.withAdministratorName(administratorName)
			.withApplicants(applicants)
			.withSchool(school)
			.withSchoolUnit(schoolUnit)
			.withEmailStatus(emailStatus)
			.withSmsStatus(smsStatus)
			.withApplicantCapacity(applicantCapacity)
			.withDaycarePlacement(daycarePlacement)
			.withDaycarePlacementChanged(daycarePlacementChanged)
			.withDaycarePlacementChangedFrom(daycarePlacementChangedFrom)
			.withContactInfo(contactInfo)
			.build();

		// Assert
		assertThat(oepErrandItem).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(oepErrandItem.getFlowInstanceId()).isEqualTo(flowInstanceId);
		assertThat(oepErrandItem.getStatus()).isEqualTo(status);
		assertThat(oepErrandItem.getMinorIdentifier()).isEqualTo(minorIdentifier);
		assertThat(oepErrandItem.getMinorName()).isEqualTo(minorName);
		assertThat(oepErrandItem.getDecisionStart()).isEqualTo(decisionStart);
		assertThat(oepErrandItem.getDecisionEnd()).isEqualTo(decisionEnd);
		assertThat(oepErrandItem.getDecision()).isEqualTo(decision);
		assertThat(oepErrandItem.getAdministratorName()).isEqualTo(administratorName);
		assertThat(oepErrandItem.getSchoolUnit()).isEqualTo(schoolUnit);
		assertThat(oepErrandItem.getEmailStatus()).isEqualTo(emailStatus);
		assertThat(oepErrandItem.getSmsStatus()).isEqualTo(smsStatus);
		assertThat(oepErrandItem.getApplicantCapacity()).isEqualTo(applicantCapacity);
		assertThat(oepErrandItem.getDaycarePlacement()).isEqualTo(daycarePlacement);
		assertThat(oepErrandItem.getDaycarePlacementChanged()).isEqualTo(daycarePlacementChanged);
		assertThat(oepErrandItem.getDaycarePlacementChangedFrom()).isEqualTo(daycarePlacementChangedFrom);
		assertThat(oepErrandItem.getContactInfo()).isEqualTo(contactInfo);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new OepErrandItem()).hasAllNullFieldsOrProperties();
		assertThat(OepErrandItem.builder().build()).hasAllNullFieldsOrProperties();
	}

}
