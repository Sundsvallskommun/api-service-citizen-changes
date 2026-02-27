package se.sundsvall.citizenchanges.apptest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import se.sundsvall.citizenchanges.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(files = "classpath:/RelocationIT/", classes = Application.class)
class RelocationIT extends AbstractAppTest {

	private static final String MUNICIPALITY_ID = "2281";

	private static final String PATH = "/" + MUNICIPALITY_ID + "/relocations/batchtrigger/relocations?backtrackDays=1";

	private static final String PATH_RECENT_MOVES = "/" + MUNICIPALITY_ID + "/relocations/meta/recentmoves?backtrackDays=1";

	@Test
	void test1_runBatch() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath(PATH)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_runBatchWithCitizenData() {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath(PATH)
			.withExpectedResponseStatus(OK)
			.withRequest("request.json")
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test3_getRecentMoves() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath(PATH_RECENT_MOVES)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

}
