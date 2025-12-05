package com.licensevalidator.service;

import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import io.jwt4j.lite.core.model.dto.reponse.ExtractClaimResponse;
import io.jwt4j.lite.core.model.dto.reponse.VerifyTokenResponse;
import io.jwt4j.lite.core.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("토큰 관련 모든 검사 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class VerifyTokenServiceTest {
	@Mock
	private TokenService tokenService;

	@InjectMocks
	private VerifyTokenService verifyTokenService;

	@Order(1)
	@Test
	@DisplayName("01. verify() - 정상 검증 성공")
	void verify_success() {
		// given
		String token = "test-token";

		VerifyTokenResponse response = mock(VerifyTokenResponse.class);
		when(response.getResultCode()).thenReturn(String.valueOf(HttpStatus.OK.value()));
		when(tokenService.verifyJwt(anyString())).thenReturn(response);

		// when & then
		assertDoesNotThrow(() -> verifyTokenService.verify(token));
	}

	@Order(2)
	@Test
	@DisplayName("02. verify() - 검증 실패 시 예외 발생")
	void verify_fail() {
		// given
		String token = "test-token";

		VerifyTokenResponse response = mock(VerifyTokenResponse.class);
		when(response.getResultCode()).thenReturn("400");
		when(response.getResultMsg()).thenReturn("invalid signature");
		when(tokenService.verifyJwt(anyString())).thenReturn(response);

		// when & then
		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyTokenService.verify(token)
		);

		assertEquals(LicenseVerifyError.FAILED_LICENSE_FORGERY_VERIFY, ex.getError());
		assertEquals("invalid signature", ex.getMessage());
	}

	@Order(3)
	@Test
	@DisplayName("03. extractClaims() - 정상 추출 성공")
	void extractClaims_success() {
		// given
		String token = "test-token";

		ExtractClaimResponse response = mock(ExtractClaimResponse.class);
		when(response.getResultCode()).thenReturn(String.valueOf(HttpStatus.OK.value()));
		when(response.getClaims()).thenReturn(mockClaims());

		when(tokenService.extractClaimToJwt(anyString())).thenReturn(response);

		// when
		Map<String, Object> result = verifyTokenService.extractClaims(token);

		// then
		assertEquals("testUser", result.get("name"));
		assertEquals("PRODLICENSE", result.get("type"));
	}

	@Order(4)
	@Test
	@DisplayName("04. extractClaims() - 추출 실패 시 예외 발생")
	void extractClaims_fail() {
		// given
		ExtractClaimResponse response = mock(ExtractClaimResponse.class);
		when(response.getResultCode()).thenReturn("500");
		when(response.getResultMsg()).thenReturn("claim parse failed");
		when(tokenService.extractClaimToJwt(anyString())).thenReturn(response);

		// when & then
		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyTokenService.extractClaims("token")
		);

		assertEquals(LicenseVerifyError.FAILED_LICENSE_DATA_EXTRACT, ex.getError());
		assertEquals("claim parse failed", ex.getMessage());
	}

	/**
	 * Mock Claims 구조 생성
	 * @return Claims 맵
	 */
	private Map<String, Object> mockClaims() {
		Map<String, Object> publicClaim = new HashMap<>();
		publicClaim.put("name", "testUser");
		publicClaim.put("type", "PRODLICENSE");

		Map<String, Object> publicClaimsWrapper = new HashMap<>();
		publicClaimsWrapper.put("publicClaim", publicClaim);

		Map<String, Object> root = new HashMap<>();
		root.put("publicClaims", publicClaimsWrapper);

		return root;
	}
}