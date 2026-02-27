package se.sundsvall.citizenchanges.apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import se.sundsvall.citizenchanges.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(
	files = "classpath:/ReminderIT/",
	classes = Application.class
)
class ReminderIT extends AbstractAppTest {

	private static final String MUNICIPALITY_ID = "2281";


	private static final String PATH = "/" + MUNICIPALITY_ID + "/reminder/batchtrigger/reminder/endofterm?firstErrand=1&numOfErrands=1&sendMessage=true";

	@Test
	void test1_runReminderBatch() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath(PATH)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

	@Test
	void test2_runReminderBatchWithOepData() {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath(PATH)
			.withRequest("request.json")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

	@Test
	void test3_runReminderBatchDryrun() {


		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/" + MUNICIPALITY_ID + "/reminder/batchtrigger/reminder/endofterm/dryrun?firstErrand=1&numOfErrands=1&email=mail%40example.com&sms=0701740605")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();

	}

}
