package se.sundsvall.citizenchanges;

import generated.se.sundsvall.citizen.CitizenAddress;
import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import generated.se.sundsvall.citizen.CustodyChildrenPupil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import se.sundsvall.citizenchanges.api.model.AddressItem;
import se.sundsvall.citizenchanges.api.model.ApplicantInfo;
import se.sundsvall.citizenchanges.api.model.ContactInfo;
import se.sundsvall.citizenchanges.api.model.DaycareInvestigationItem;
import se.sundsvall.citizenchanges.api.model.InvestigationItem;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;
import se.sundsvall.citizenchanges.api.model.ReportMetaData;

public final class TestDataFactory {

	private TestDataFactory() {}

	public static OepErrandItem buildOepErrandItem(final String daycarePlacement, final String applicantIdentifier, final String minorIdentifier, LocalDate decisionEnd) {
		final var errand = buildOepErrandItem(daycarePlacement, applicantIdentifier, minorIdentifier);
		errand.setDecisionEnd(decisionEnd == null ? null : decisionEnd.toString());
		return errand;
	}

	public static OepErrandItem buildOepErrandItem(final String daycarePlacement, final String applicantIdentifier, final String minorIdentifier) {
		return OepErrandItem.builder()
			.withFlowInstanceId("someFlowInstanceId")
			.withStatus("someStatus")
			.withApplicants(List.of(ApplicantInfo.builder()
				.withApplicantIdentifier(applicantIdentifier)
				.withApplicantName("someApplicantName")
				.withFirstName("someFirstname")
				.withLastName("someLastName")
				.withPrimaryGuardian(true)
				.build()))
			.withMinorIdentifier(minorIdentifier)
			.withMinorName("someMinorName")
			.withDecisionStart(LocalDate.now().minusYears(1).toString())
			.withDecisionEnd(LocalDate.now().plusMonths(3).toString())
			.withFamilyId("someFamilyId")
			.withDecision("someDecision")
			.withAdministratorName("someAdministratorName")
			.withSchool("someSchool")
			.withSchoolUnit("someSchoolUnit")
			.withContactInfo(ContactInfo.builder()
				.withEmailAddress("someEmailAddress")
				.withPhoneNumber("076-1234567")
				.withContactBySMS(true)
				.withDisplayName("someDisplayName")
				.withFirstName("someFirstname")
				.withLastName("someLastname")
				.build())
			.withEmailStatus("someEmailStatus")
			.withSmsStatus("someSmsStatus")
			.withApplicantCapacity("someApplicantCapacity")
			.withDaycarePlacement(daycarePlacement)
			.withDaycarePlacementChanged("DaycarePlacementChanged")
			.withDaycarePlacementChangedFrom("daycarePlacementChangedFrom")
			.build();
	}

	public static CitizenWithChangedAddress buildCitizen(final String applicantIdentifier) {
		return new CitizenWithChangedAddress()
			.classified("someClassified")
			.gender("someGender")
			.lastname("someLastname")
			.givenname("someGivenName")
			.personId(UUID.randomUUID())
			.personNumber(applicantIdentifier)
			.custodianFor(Collections.singletonList(new CustodyChildrenPupil().personnumber("somePersonnummer").typeOfSchool("someTypeOfSchool")))
			.addresses(List.of(new CitizenAddress()
				.status("Current")
				.nrDate(LocalDateTime.now())
				.address("someAddress")
				.co("someCo")
				.appartmentNumber("someApartmentNumber")
				.postalCode("somePostalCode")
				.city("some")
				.county("81")
				.municipality("22")
				.addressType("someAddressType")
				.xCoordLocal(12D)
				.yCoordLocal(14D), new CitizenAddress()
					.status("Previous")
					.nrDate(LocalDateTime.now())
					.address("someAddress")
					.co("someCo")
					.appartmentNumber("someApartmentNumber")
					.postalCode("somePostalCode")
					.city("some")
					.county("81")
					.municipality("22")
					.addressType("someAddressType")
					.xCoordLocal(1D)
					.yCoordLocal(123D)));
	}

	public static DaycareInvestigationItem buildDaycareInvestigationItem() {
		return DaycareInvestigationItem.builder()
			.withIstPlacement("someIstPlacement")
			.withIstChangeStartDate("someIstChangeStartDate")
			.withIstPlacementEndDate("someIstPlacementEndDate")
			.withIstPlacementName("someIstPlacementName")
			.withFlowInstanceId("someFlowInstanceId")
			.withStatus("someStatus")
			.withMinorIdentifier("someMinorIdentifier")
			.withMinorName("someMinorName")
			.withDecisionStart("someDecisionStart")
			.withDecisionEnd("someDecisionEnd")
			.withFamilyId("someFamilyId")
			.withDecision("someDecision")
			.withApplicants(List.of(ApplicantInfo.builder().build()))
			.withSchool("someSchool")
			.withAdministratorName("someAdministratorName")
			.withSchoolUnit("someSchoolUnit")
			.withEmailStatus("someEmailStatus")
			.withSmsStatus("someSmsStatus")
			.withApplicantCapacity("someApplicantCapacity")
			.withDaycarePlacement("someDaycarePlacement")
			.withDaycarePlacementChanged("someDaycarePlacementChanged")
			.withDaycarePlacementChangedFrom("someDaycarePlacementChangedFrom")
			.withContactInfo(ContactInfo.builder().build())
			.build();
	}

	public static InvestigationItem buildInvestigationItem() {
		return InvestigationItem.builder()
			.withFlowInstanceId("someFlowInstanceId")
			.withFamilyId("someFamilyId")
			.withDecisionStart("someDecisionStart")
			.withDecisionEnd("someDecisionEnd")
			.withNewAddress(AddressItem.builder().build())
			.withOldAddress(AddressItem.builder().build())
			.withNameGuardian("someNameGuardian")
			.withNameMinor("someNameMinor")
			.withStatus("someStatus")
			.withSchoolProgram("someSchoolProgram")
			.withClassified("someClassified")
			.withAdministratorName("someAdministratorName")
			.withSchoolUnit("someSchoolUnit")
			.withMinorToSameAddress("someMinorToSameAddress")
			.withMoverList("someMoverList")
			.build();
	}

	public static ReportMetaData buildReportMetaData() {
		return ReportMetaData.builder()
			.withReportType("someReportType")
			.withReportTimestamp("someReportTimestamp")
			.withMetaStartDate("someMetaStartDate")
			.withEduCloudStartDate("someEduCloudStartDate")
			.withOepStartDate("someOepStartDate")
			.withChangedAddressesCount(1)
			.withInspectErrandsCount(1)
			.build();
	}
}
