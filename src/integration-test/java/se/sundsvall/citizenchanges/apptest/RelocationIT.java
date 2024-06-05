package se.sundsvall.citizenchanges.apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import se.sundsvall.citizenchanges.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(files = "classpath:/RelocationIT/", classes = Application.class)
class RelocationIT extends AbstractAppTest {

	@Test
	void test1_runBatch() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/relocations/batchtrigger/relocations?backtrackDays=1")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_runBatchWithCitizenData() {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath("/relocations/batchtrigger/relocations?backtrackDays=1")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withRequest("request.json")
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test3_getRecentMoves() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/relocations/meta/recentmoves?backtrackDays=1")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

}
