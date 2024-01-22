package se.sundsvall.citizenchanges.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.citizenchanges.CitizenChanges;

@SpringBootTest(classes = CitizenChanges.class)
@ActiveProfiles("junit")
class ServicePropertiesTest {

	@Autowired
	private ServiceProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.familyId()).isEqualTo("344,349");
	}


}
