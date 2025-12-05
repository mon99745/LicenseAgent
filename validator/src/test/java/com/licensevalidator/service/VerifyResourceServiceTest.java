package com.licensevalidator.service;

import com.licensecommon.enums.LicenseType;
import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("라이선스 타입별 비즈니스 검증 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VerifyResourceServiceTest {
	private VerifyResourceService verifyResourceService;

	@BeforeEach
	void setUp() {
		verifyResourceService = new VerifyResourceService();
	}

	@Order(1)
	@DisplayName("01. PRODLICENSE - IP 일치 시 성공")
	@Test
	void prodLicense_success_whenIpMatches() throws Exception {
		// given
		String localIp = InetAddress.getLocalHost().getHostAddress();

		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.PRODLICENSE.getKey());
		claims.put("ipAddress", localIp);

		// when & then
		assertDoesNotThrow(() -> verifyResourceService.verify(claims));
	}

	@Order(2)
	@DisplayName("02. PRODLICENSE - IP 불일치 시 실패")
	@Test
	void prodLicense_fail_whenIpNotMatch() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.PRODLICENSE.getKey());
		claims.put("ipAddress", "0.0.0.0");

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(LicenseVerifyError.INVALID_LICENSE.getCode(), ex.getError().getCode());
	}

	@Order(3)
	@DisplayName("03. PRODLICENSE - ipAddress 누락 시 실패")
	@Test
	void fail_whenIpAddressMissing_forProdLicense() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.PRODLICENSE.getKey());

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(LicenseVerifyError.EMPTY_VALID_VALUE_IPADDRESS.getCode(), ex.getError().getCode());
	}


	@Order(4)
	@DisplayName("04. DEVLICENSE - 유효기간 정상일 때 성공")
	@Test
	void devLicense_success_whenNotExpired() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.DEVLICENSE.getKey());
		claims.put("expDate", "2999-12-31");

		assertDoesNotThrow(() -> verifyResourceService.verify(claims));
	}

	@Order(5)
	@DisplayName("05. DEVLICENSE - 유효기간 만료 시 실패")
	@Test
	void devLicense_fail_whenExpired() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.DEVLICENSE.getKey());
		claims.put("expDate", "2000-01-01");

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(
				LicenseVerifyError.INVALID_LICENSE_EXPIRE_DATE.getCode(),
				ex.getError().getCode()
		);
	}

	@Order(6)
	@DisplayName("06. DEVLICENSE - expDate 누락 시 실패")
	@Test
	void fail_whenExpDateMissing_forDevLicense() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", LicenseType.DEVLICENSE.getKey());

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(LicenseVerifyError.EMPTY_VALID_VALUE_EXPIRE_DATE.getCode(), ex.getError().getCode());
	}

	@Order(7)
	@DisplayName("07. type 누락 시 실패")
	@Test
	void fail_whenTypeIsMissing() {
		Map<String, Object> claims = new HashMap<>();

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(LicenseVerifyError.EMPTY_VALID_VALUE_OPERATION.getCode(), ex.getError().getCode());
	}

	@Order(8)
	@DisplayName("08. 잘못된 type 값일 때 실패")
	@Test
	void fail_whenLicenseTypeIsInvalid() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", "UNKNOWN");
		claims.put("expDate", "2999-12-31");

		LicenseVerifyException ex = assertThrows(
				LicenseVerifyException.class,
				() -> verifyResourceService.verify(claims)
		);

		assertEquals(
				LicenseVerifyError.INVALID_LICENSE_FILE_TYPE.getCode(),
				ex.getError().getCode()
		);
	}
}