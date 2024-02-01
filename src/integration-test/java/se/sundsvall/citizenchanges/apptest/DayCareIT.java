package se.sundsvall.citizenchanges.apptest;


import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import se.sundsvall.citizenchanges.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

@WireMockAppTestSuite(
	files = "classpath:/DayCareIT/",
	classes = Application.class
)
class DayCareIT extends AbstractAppTest {


	@Test
	void test1_runDaycareCheckBatch() throws FileNotFoundException {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath("/daycare/batchtrigger/daycarechecker?firstErrand=1&numOfErrands=1&backtrackDays=1")
			.withContentType(MediaType.valueOf("multipart/form-data"))
			.withRequestFile("file", "mockfile.xls")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_checkCachedFile() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath("/daycare/cachedFile")
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("true")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test3_deleteCachedFile() {

		setupCall()
			.withHttpMethod(HttpMethod.DELETE)
			.withServicePath("/daycare/cachedFile")
			.withExpectedResponseStatus(HttpStatus.OK)
			.sendRequestAndVerifyResponse();
	}

}
