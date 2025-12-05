package com.licensevalidator.service;

import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import com.licensevalidator.model.response.LicenseVerifyResponse;
import io.jwt4j.lite.core.exception.TokenError;
import io.jwt4j.lite.core.exception.TokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("라이선스 검증 흐름 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LicenseVerifyServiceTest {
	@Mock
	private VerifyFileService verifyFileService;

	@Mock
	private VerifyTokenService verifyTokenService;

	@Mock
	private VerifyResourceService verifyResourceService;

	@InjectMocks
	private LicenseVerifyService licenseVerifyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Order(1)
	@DisplayName("01. 정상 시나리오 케이스")
	@Test
	void verify_success() {
		String path = "/test.lic";
		File mockFile = new File(path);
		String token = "mock-token";
		Map<String, Object> claims = Map.of("type", "PROD");

		// Mocking
		when(verifyFileService.validate(path)).thenReturn(mockFile);
		when(verifyFileService.readToken(mockFile)).thenReturn(token);
		doNothing().when(verifyTokenService).verify(token);
		when(verifyTokenService.extractClaims(token)).thenReturn(claims);
		doNothing().when(verifyResourceService).verify(claims);

		// Execute
		LicenseVerifyResponse res = licenseVerifyService.verify(path);

		// Validate
		assertThat(res.isValid()).isTrue();
		assertThat(res.getResultCode()).isEqualTo(String.valueOf(HttpStatus.OK.value()));

		verify(verifyFileService).validate(path);
		verify(verifyFileService).readToken(mockFile);
		verify(verifyTokenService).verify(token);
		verify(verifyTokenService).extractClaims(token);
		verify(verifyResourceService).verify(claims);
	}

	@Order(2)
	@DisplayName("02. LicenseVerifyException 발생 케이스")
	@Test
	void verify_licenseVerifyException() {
		String path = "/test.lic";

		// Mock: validate 과정에서 예외 발생
		LicenseVerifyException ex = new LicenseVerifyException(
				LicenseVerifyError.INVALID_LICENSE, "Invalid"
		);
		when(verifyFileService.validate(path)).thenThrow(ex);

		LicenseVerifyResponse res = licenseVerifyService.verify(path);

		assertThat(res.isValid()).isFalse();
		assertThat(res.getResultCode()).isEqualTo(ex.getError().getCode());
		assertThat(res.getResultMsg()).isEqualTo(ex.getMessage());
	}

	@Order(3)
	@DisplayName("03. TokenException 발생 케이스")
	@Test
	void verify_tokenException() {
		String path = "/test.lic";
		File mockFile = new File(path);
		String token = "mock-token";

		when(verifyFileService.validate(path)).thenReturn(mockFile);
		when(verifyFileService.readToken(mockFile)).thenReturn(token);

		TokenException ex = new TokenException(
				TokenError.INVALID_TOKEN, "Invalid token"
		);
		doThrow(ex).when(verifyTokenService).verify(token);

		LicenseVerifyResponse res = licenseVerifyService.verify(path);

		assertThat(res.isValid()).isFalse();
		assertThat(res.getResultCode()).isEqualTo(ex.getError().getCode());
		assertThat(res.getResultMsg()).isEqualTo(ex.getMessage());
	}
}