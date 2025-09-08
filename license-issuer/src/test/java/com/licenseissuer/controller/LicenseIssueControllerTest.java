package com.licenseissuer.controller;

import com.licenseissuer.annotation.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static com.licenseissuer.controller.LicenseIssueController.PATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
public class LicenseIssueControllerTest {
	private static final MockHttpSession SESSION = new MockHttpSession();

	@Autowired
	private MockMvc mvc;

	private static String issueContent_prod = "{\n" +
			"  \"operation\": \"prod\",\n" +
			"  \"projectName\": \"TestProject\",\n" +
			"  \"ipAddress\": \"192.168.0.1\",\n" +
			"  \"expDate\": \"2025-12-31 23:59:59\",\n" +
			"  \"issuer\": \"Tester\",\n" +
			"  \"issuerIp\": \"\"\n" +
			"}";

	private static String issueContent_dev = "{\n" +
			"  \"operation\": \"dev\",\n" +
			"  \"projectName\": \"TestProject\",\n" +
			"  \"ipAddress\": \"192.168.0.1\",\n" +
			"  \"expDate\": \"2025-12-31 23:59:59\",\n" +
			"  \"issuer\": \"Tester\",\n" +
			"  \"issuerIp\": \"\"\n" +
			"}";

	private static String issueContent_temp = "{\n" +
			"  \"operation\": \"temp\",\n" +
			"  \"projectName\": \"TestProject\",\n" +
			"  \"ipAddress\": \"192.168.0.1\",\n" +
			"  \"expDate\": \"2025-12-31 23:59:59\",\n" +
			"  \"issuer\": \"Tester\",\n" +
			"  \"issuerIp\": \"\"\n" +
			"}";
	@Test
	void t01_운영_라이센스_다운로드() throws Exception {
		MvcResult result = mvc.perform(post(PATH + "/download")
						.contentType(MediaType.APPLICATION_JSON)
						.content(issueContent_prod))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		// Response에서 파일 내용 가져오기
		MockHttpServletResponse response = result.getResponse();
		byte[] contentBytes = response.getContentAsByteArray();

		// 파일이 반환되었는지 확인
		assertThat(contentBytes).isNotEmpty();

		// Content-Disposition 헤더 확인 (파일 이름)
		String fileName = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
//		assertThat(contentDisposition).contains("attachment");

		// 필요하면 파일 내용을 문자열로 확인
		String fileContent = new String(contentBytes, StandardCharsets.UTF_8);
//		assertThat(fileContent).contains("라이선스명"); // 기대 문자열 포함 여부 체크

	}

	@Test
	void t02_개발_라이센스_다운로드() throws Exception {
		MvcResult result = mvc.perform(post(PATH + "/download")
						.contentType(MediaType.APPLICATION_JSON)
						.content(issueContent_dev))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		// Response에서 파일 내용 가져오기
		MockHttpServletResponse response = result.getResponse();
		byte[] contentBytes = response.getContentAsByteArray();

		// 파일이 반환되었는지 확인
		assertThat(contentBytes).isNotEmpty();

		// Content-Disposition 헤더 확인 (파일 이름)
		String fileName = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
//		assertThat(contentDisposition).contains("attachment");

		// 필요하면 파일 내용을 문자열로 확인
		String fileContent = new String(contentBytes, StandardCharsets.UTF_8);
//		assertThat(fileContent).contains("라이선스명"); // 기대 문자열 포함 여부 체크

	}

	@Test
	void t03_임시_라이센스_다운로드() throws Exception {
		MvcResult result = mvc.perform(post(PATH + "/download")
						.contentType(MediaType.APPLICATION_JSON)
						.content(issueContent_temp))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		// Response에서 파일 내용 가져오기
		MockHttpServletResponse response = result.getResponse();
		byte[] contentBytes = response.getContentAsByteArray();

		// 파일이 반환되었는지 확인
		assertThat(contentBytes).isNotEmpty();

		// Content-Disposition 헤더 확인 (파일 이름)
		String fileName = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
//		assertThat(contentDisposition).contains("attachment");

		// 필요하면 파일 내용을 문자열로 확인
		String fileContent = new String(contentBytes, StandardCharsets.UTF_8);
//		assertThat(fileContent).contains("라이선스명"); // 기대 문자열 포함 여부 체크

	}
}