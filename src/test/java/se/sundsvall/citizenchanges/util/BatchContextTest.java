package se.sundsvall.citizenchanges.util;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class BatchContextTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(BatchContext.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilder() {
		// Arrange
		final var firstErrand = 1;
		final var numberOfErrands = 1;
		final var sendMessages = true;
		final var oepErrandIds = List.of("someOepErrandId");
		final var sms = "someSms";
		final var email = "someEmail";

		// Act
		final var batch = BatchContext
			.builder()
			.withFirstErrand(firstErrand)
			.withNumberOfErrands(numberOfErrands)
			.withSendMessages(sendMessages)
			.withOepErrandIds(oepErrandIds)
			.withSms(sms)
			.withEmail(email)
			.build();

		// Assert
		assertThat(batch)
			.hasNoNullFieldsOrProperties()
			.extracting(
				"firstErrand",
				"numberOfErrands",
				"sendMessages",
				"oepErrandIds",
				"sms",
				"email")
			.containsExactlyInAnyOrder(
				firstErrand,
				numberOfErrands,
				sendMessages,
				oepErrandIds,
				sms,
				email);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(new BatchContext())
			.hasAllNullFieldsOrPropertiesExcept("firstErrand", "numberOfErrands", "sendMessages")
			.satisfies(bean -> {
				Assertions.assertThat(bean.getFirstErrand()).isZero();
				Assertions.assertThat(bean.getNumberOfErrands()).isZero();
				Assertions.assertThat(bean.isSendMessages()).isFalse();
			});
		Assertions.assertThat(BatchContext.builder().build())
			.hasAllNullFieldsOrPropertiesExcept("firstErrand", "numberOfErrands", "sendMessages")
			.satisfies(bean -> {
				Assertions.assertThat(bean.getFirstErrand()).isZero();
				Assertions.assertThat(bean.getNumberOfErrands()).isZero();
				Assertions.assertThat(bean.isSendMessages()).isFalse();
			});
	}

}
