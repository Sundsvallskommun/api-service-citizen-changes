package se.sundsvall.citizenchanges.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.citizenchanges.Application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("junit")
class MessageMapperPropertiesTest {

	@Autowired
	private MessageMapperProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.recipientsSkolskjuts()).isEqualTo("someemail@test.se");
		assertThat(properties.recipientsElevresa()).isEqualTo("someemail@test.se");
		assertThat(properties.linkTemplate()).isEqualTo("http://link.template");
	}

}
