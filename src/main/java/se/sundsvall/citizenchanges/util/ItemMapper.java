package se.sundsvall.citizenchanges.util;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import generated.se.sundsvall.citizen.CitizenAddress;
import generated.se.sundsvall.citizen.CitizenWithChangedAddress;
import se.sundsvall.citizenchanges.api.model.AddressItem;
import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.api.model.InvestigationItem;
import se.sundsvall.citizenchanges.api.model.OepErrandItem;

@Component
public class ItemMapper {

	private static final String CURRENT_ADDRESS = "Current";

	public InvestigationItem composeInvestigationItem(final CitizenWithChangedAddress thisMove, final OepErrandItem item,
		final FamilyType familyType, final String moverIdentifier, final boolean sameAddress) {

		final var addressMap =
			thisMove.getAddresses().stream().collect(Collectors.toMap(CitizenAddress::getStatus, Function.identity()));
		final var county = addressMap.get(CURRENT_ADDRESS).getCounty();
		final var municipality = addressMap.get(CURRENT_ADDRESS).getMunicipality();

		final var thisItem = InvestigationItem.builder()
			.withFlowInstanceId(item.getFlowInstanceId())
			.withFamilyId(item.getFamilyId())
			.withDecisionStart(item.getDecisionStart())
			.withDecisionEnd(item.getDecisionEnd())
			.withNewAddress(toAdressItem(addressMap.get(CURRENT_ADDRESS)))
			.withOldAddress(toAdressItem(addressMap.get("Previous")))
			.withClassified(thisMove.getClassified().equalsIgnoreCase("J") ? Constants.STATUS_YES : Constants.STATUS_NO)
			.withStatus((!Objects.equals(county, Constants.SUNDSVALL_COUNTY_CODE) || !Objects.equals(municipality, Constants.SUNDSVALL_MUNICIPALITY_CODE)) ? Constants.STATUS_UTFLYTTAD : null)
			.withMinorToSameAddress(sameAddress ? Constants.STATUS_YES : Constants.STATUS_NO)
			.withSchoolUnit(item.getSchool())
			.withSchoolProgram(item.getSchoolUnit())
			.withAdministratorName(item.getAdministratorName())
			.build();

		if (familyType.equals(FamilyType.SKOLSKJUTS)) {
			final var applicants = item.getApplicants();
			final var applicant = applicants.stream()
				.filter(applicantInfo -> moverIdentifier.equals(applicantInfo.getApplicantIdentifier()))
				.findAny()
				.orElse(null);

			if (applicant != null) {
				final var primaryGuardianIndicator = applicant.isPrimaryGuardian() ? "" : "2";
				thisItem.setNameGuardian(applicant.getApplicantName() + " (" + Constants.GUARDIAN_NOTATION + primaryGuardianIndicator + ")");
				thisItem.setNameMinor(item.getMinorName() + " (" + Constants.MINOR_NOTATION + ")");
				thisItem.setMoverList(Constants.GUARDIAN_NOTATION + primaryGuardianIndicator + (sameAddress ? "," + Constants.MINOR_NOTATION : ""));
			} else if (moverIdentifier.equals(item.getMinorIdentifier())) {
				thisItem.setNameMinor(item.getMinorName() + " (" + Constants.MINOR_NOTATION + ")");
				thisItem.setMoverList(Constants.MINOR_NOTATION);
			}
		}

		return thisItem;
	}

	private AddressItem toAdressItem(final CitizenAddress citizenAddress) {

		return AddressItem.builder()
			.withStatus(citizenAddress.getStatus())
			.withNrDate(citizenAddress.getNrDate().atOffset(OffsetDateTime.now().getOffset()))
			.withAddress(citizenAddress.getAddress())
			.withCo(citizenAddress.getCo())
			.withApartmentNumber(citizenAddress.getAppartmentNumber())
			.withPostalCode(citizenAddress.getPostalCode())
			.withCity(citizenAddress.getCity())
			.withCounty(citizenAddress.getCounty())
			.withMunicipality(citizenAddress.getMunicipality())
			.withAddressType(citizenAddress.getAddressType())
			.withCoordinateX(citizenAddress.getxCoordLocal())
			.withCoordinateY(citizenAddress.getyCoordLocal())
			.build();
	}

}
