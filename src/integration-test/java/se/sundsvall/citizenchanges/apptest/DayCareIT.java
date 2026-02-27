package se.sundsvall.citizenchanges.apptest;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import se.sundsvall.citizenchanges.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@WireMockAppTestSuite(files = "classpath:/DayCareIT/", classes = Application.class)
class DayCareIT extends AbstractAppTest {

	private static final String MUNICIPALITY_ID = "2281";

	private static final String PATH = "/" + MUNICIPALITY_ID + "/daycare/batchtrigger/daycarechecker?firstErrand=1&numOfErrands=1&backtrackDays=1";

	private static final String CACHED_FILE = "/" + MUNICIPALITY_ID + "/daycare/cachedFile";

	@Test
	void test1_runDaycareCheckBatch() throws FileNotFoundException {

		setupCall()
			.withHttpMethod(HttpMethod.POST)
			.withServicePath(PATH)
			.withContentType(MediaType.valueOf("multipart/form-data"))
			.withRequestFile("file", "mockfile.xls")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("DONE")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_checkCachedFile() {

		setupCall()
			.withHttpMethod(HttpMethod.GET)
			.withServicePath(CACHED_FILE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("true")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test3_deleteCachedFile() {

		setupCall()
			.withHttpMethod(HttpMethod.DELETE)
			.withServicePath(CACHED_FILE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequestAndVerifyResponse();
	}

}
