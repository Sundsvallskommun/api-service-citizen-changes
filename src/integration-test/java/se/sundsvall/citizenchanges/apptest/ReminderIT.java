package se.sundsvall.citizenchanges.apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import se.sundsvall.citizenchanges.CitizenChanges;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(
	files = "classpath:/ReminderIT/",
	classes = CitizenChanges.class
)
class ReminderIT extends AbstractAppTest {

	@Test
	void test1_runReminderBatch() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/reminder/batchtrigger/reminder/endofterm?firstErrand=1&numOfErrands=1&sendMessage=true")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

	@Test
	void test2_runReminderBatchWithOepData() {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath("/reminder/batchtrigger/reminder/endofterm?firstErrand=1&numOfErrands=1&sendMessage=true")
			.withRequest("request.json")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

	@Test
	void test3_runReminderBatchDryrun() {


		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/reminder/batchtrigger/reminder/endofterm/dryrun?firstErrand=1&numOfErrands=1&email=mail%40example.com&sms=0701234567")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

}
