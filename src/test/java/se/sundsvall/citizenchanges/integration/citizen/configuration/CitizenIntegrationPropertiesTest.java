package se.sundsvall.citizenchanges.integration.citizen.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.citizenchanges.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class CitizenIntegrationPropertiesTest {

	@Autowired
	private CitizenIntegrationProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.connectTimeout()).isEqualTo(5);
		assertThat(properties.readTimeout()).isEqualTo(30);
	}

}
