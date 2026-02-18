package se.sundsvall.citizenchanges.api.model;

import java.time.OffsetDateTime;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

class AddressItemTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(AddressItem.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		// Arrange
		final var status = "status";
		final var nrDate = OffsetDateTime.now();
		final var address = "address";
		final var co = "co";
		final var apartmentNumber = "apartmentNumber";
		final var postalCode = "postalCode";
		final var city = "city";
		final var county = "81";
		final var municipality = "22";
		final var addressType = "addressType";
		final var coordinateX = 12D;
		final var coordinateY = 14D;

		// Act
		final var bean = AddressItem.builder()
			.withStatus(status)
			.withNrDate(nrDate)
			.withAddress(address)
			.withCo(co)
			.withApartmentNumber(apartmentNumber)
			.withPostalCode(postalCode)
			.withCity(city)
			.withCounty(county)
			.withMunicipality(municipality)
			.withAddressType(addressType)
			.withCoordinateX(coordinateX)
			.withCoordinateY(coordinateY)
			.build();

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getStatus()).isEqualTo(status);
		assertThat(bean.getNrDate()).isEqualTo(nrDate);
		assertThat(bean.getAddress()).isEqualTo(address);
		assertThat(bean.getCo()).isEqualTo(co);
		assertThat(bean.getApartmentNumber()).isEqualTo(apartmentNumber);
		assertThat(bean.getPostalCode()).isEqualTo(postalCode);
		assertThat(bean.getCity()).isEqualTo(city);
		assertThat(bean.getCounty()).isEqualTo(county);
		assertThat(bean.getMunicipality()).isEqualTo(municipality);
		assertThat(bean.getAddressType()).isEqualTo(addressType);
		assertThat(bean.getCoordinateX()).isEqualTo(coordinateX);
		assertThat(bean.getCoordinateY()).isEqualTo(coordinateY);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new AddressItem()).hasAllNullFieldsOrProperties();
		assertThat(AddressItem.builder().build()).hasAllNullFieldsOrProperties();
	}

}
