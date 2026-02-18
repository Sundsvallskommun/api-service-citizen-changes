package se.sundsvall.citizenchanges.api.model;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

class InvestigationItemTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(InvestigationItem.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var flowInstanceId = "someFlowInstanceId";
		final var familyId = "someFamilyId";
		final var decisionStart = "someDecisionStart";
		final var decisionEnd = "someDecisionEnd";
		final var newAddress = AddressItem.builder().build();
		final var oldAddress = AddressItem.builder().build();
		final var nameGuardian = "someNameGuardian";
		final var nameMinor = "someNameMinor";
		final var status = "someStatus";
		final var schoolProgram = "someSchoolProgram";
		final var classified = "someClassified";
		final var administratorName = "someAdministratorName";
		final var schoolUnit = "someSchoolUnit";
		final var minorToSameAddress = "someMinorToSameAddress";
		final var moverList = "someMoverList";

		// Act
		final var investigationItem = InvestigationItem.builder()
			.withFlowInstanceId(flowInstanceId)
			.withFamilyId(familyId)
			.withDecisionStart(decisionStart)
			.withDecisionEnd(decisionEnd)
			.withNewAddress(newAddress)
			.withOldAddress(oldAddress)
			.withNameGuardian(nameGuardian)
			.withNameMinor(nameMinor)
			.withStatus(status)
			.withSchoolProgram(schoolProgram)
			.withClassified(classified)
			.withAdministratorName(administratorName)
			.withSchoolUnit(schoolUnit)
			.withMinorToSameAddress(minorToSameAddress)
			.withMoverList(moverList)
			.build();

		// Assert
		assertThat(investigationItem).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(investigationItem.getFlowInstanceId()).isEqualTo(flowInstanceId);
		assertThat(investigationItem.getFamilyId()).isEqualTo(familyId);
		assertThat(investigationItem.getDecisionStart()).isEqualTo(decisionStart);
		assertThat(investigationItem.getDecisionEnd()).isEqualTo(decisionEnd);
		assertThat(investigationItem.getNameGuardian()).isEqualTo(nameGuardian);
		assertThat(investigationItem.getNameMinor()).isEqualTo(nameMinor);
		assertThat(investigationItem.getClassified()).isEqualTo(classified);
		assertThat(investigationItem.getStatus()).isEqualTo(status);
		assertThat(investigationItem.getAdministratorName()).isEqualTo(administratorName);
		assertThat(investigationItem.getSchoolUnit()).isEqualTo(schoolUnit);
		assertThat(investigationItem.getMinorToSameAddress()).isEqualTo(minorToSameAddress);
		assertThat(investigationItem.getMoverList()).isEqualTo(moverList);
		assertThat(investigationItem.getSchoolProgram()).isEqualTo(schoolProgram);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new InvestigationItem()).hasAllNullFieldsOrProperties();
		assertThat(InvestigationItem.builder().build()).hasAllNullFieldsOrProperties();
	}

}
