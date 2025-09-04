package com.licenseissuer.service;

import com.licenseissuer.annotation.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@ServiceTest
@DisplayName("라이센스 발급 서비스 테스트")
public class IssueServiceTest {
	private static final MockHttpSession SESSION = new MockHttpSession();

	@Autowired
	private MockMvc mvc;

	@Test
	void test01() throws Exception {
	}
}