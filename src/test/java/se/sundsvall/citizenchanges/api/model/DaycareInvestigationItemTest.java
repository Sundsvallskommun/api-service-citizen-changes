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

class DaycareInvestigationItemTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(DaycareInvestigationItem.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilder() {
		// Arrange
		final var istPlacement = "someIstPlacement";
		final var istChangeStartDate = "someIstChangeStartDate";
		final var istPlacementEndDate = "someIstPlacementEndDate";
		final var istPlacementName = "someIstPlacementName";
		final var flowInstanceId = "someFlowInstanceId";
		final var status = "someStatus";
		final var minorIdentifier = "someMinorIdentifier";
		final var minorName = "someMinorName";
		final var decisionStart = "someDecisionStart";
		final var decisionEnd = "someDecisionEnd";
		final var familyId = "someFamilyId";
		final var decision = "someDecision";
		final var applicants = List.of(ApplicantInfo.builder().build());
		final var school = "someSchool";
		final var administratorName = "someAdministratorName";
		final var schoolUnit = "someSchoolUnit";
		final var emailStatus = "someEmailStatus";
		final var smsStatus = "someSmsStatus";
		final var applicantCapacity = "someApplicantCapacity";
		final var daycarePlacement = "someDaycarePlacement";
		final var daycarePlacementChanged = "someDaycarePlacementChanged";
		final var daycarePlacementChangedFrom = "someDaycarePlacementChangedFrom";
		final var contactInfo = ContactInfo.builder().build();

		// Act
		final var daycare = DaycareInvestigationItem.builder()
			.withIstPlacement(istPlacement)
			.withIstChangeStartDate(istChangeStartDate)
			.withIstPlacementEndDate(istPlacementEndDate)
			.withIstPlacementName(istPlacementName)
			.withFlowInstanceId(flowInstanceId)
			.withStatus(status)
			.withMinorIdentifier(minorIdentifier)
			.withMinorName(minorName)
			.withDecisionStart(decisionStart)
			.withDecisionEnd(decisionEnd)
			.withFamilyId(familyId)
			.withDecision(decision)
			.withApplicants(applicants)
			.withSchool(school)
			.withAdministratorName(administratorName)
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
		assertThat(daycare).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(daycare.getIstPlacement()).isEqualTo(istPlacement);
		assertThat(daycare.getIstChangeStartDate()).isEqualTo(istChangeStartDate);
		assertThat(daycare.getIstPlacementEndDate()).isEqualTo(istPlacementEndDate);
		assertThat(daycare.getIstPlacementName()).isEqualTo(istPlacementName);
		assertThat(daycare.getFlowInstanceId()).isEqualTo(flowInstanceId);
		assertThat(daycare.getStatus()).isEqualTo(status);
		assertThat(daycare.getMinorIdentifier()).isEqualTo(minorIdentifier);
		assertThat(daycare.getMinorName()).isEqualTo(minorName);
		assertThat(daycare.getDecisionStart()).isEqualTo(decisionStart);
		assertThat(daycare.getDecisionEnd()).isEqualTo(decisionEnd);
		assertThat(daycare.getFamilyId()).isEqualTo(familyId);
		assertThat(daycare.getDecision()).isEqualTo(decision);
		assertThat(daycare.getAdministratorName()).isEqualTo(administratorName);
		assertThat(daycare.getSchoolUnit()).isEqualTo(schoolUnit);
		assertThat(daycare.getEmailStatus()).isEqualTo(emailStatus);
		assertThat(daycare.getSmsStatus()).isEqualTo(smsStatus);
		assertThat(daycare.getApplicantCapacity()).isEqualTo(applicantCapacity);
		assertThat(daycare.getDaycarePlacement()).isEqualTo(daycarePlacement);
		assertThat(daycare.getDaycarePlacementChanged()).isEqualTo(daycarePlacementChanged);
		assertThat(daycare.getDaycarePlacementChangedFrom()).isEqualTo(daycarePlacementChangedFrom);
		assertThat(daycare.getContactInfo()).isEqualTo(contactInfo);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new DaycareInvestigationItem()).hasAllNullFieldsOrProperties();
		assertThat(DaycareInvestigationItem.builder().build()).hasAllNullFieldsOrProperties();
	}

}
