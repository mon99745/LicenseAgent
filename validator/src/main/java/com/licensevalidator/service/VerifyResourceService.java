package com.licensevalidator.service;

import com.licensecommon.enums.LicenseType;
import com.licensecommon.util.DateUtil;
import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 라이선스 타입별 비즈니스 검증
 */
@Slf4j
@Service
public class VerifyResourceService {
	public void verify(Map<String, Object> claims) {

		String type = getString(claims, "type");
		LicenseType licType = LicenseType.fromKey(type);

		switch (licType) {
			case PRODLICENSE ->
					validateProdLicense(claims);

			case DEVLICENSE, TEMPLICENSE ->
					validateDateLicense(claims);

			default ->
					throw new LicenseVerifyException(LicenseVerifyError.INVALID_LICENSE_FILE_TYPE, type);
		}
	}

	private void validateProdLicense(Map<String, Object> claims) {
		String ipAddress = getString(claims, "ipAddress");

		try {
			if (!ipAddress.equals(InetAddress.getLocalHost().getHostAddress())) {
				throw new LicenseVerifyException(LicenseVerifyError.INVALID_LICENSE);
			}
		} catch (UnknownHostException e) {
			throw new LicenseVerifyException(LicenseVerifyError.FAILED_LOCAL_IP_LOOKUP);
		}
	}

	private void validateDateLicense(Map<String, Object> claims) {
		String expDate = getString(claims, "expDate");

		if (new Date().after(DateUtil.parse(expDate))) {
			throw new LicenseVerifyException(
					LicenseVerifyError.INVALID_LICENSE_EXPIRE_DATE,
					expDate
			);
		}
	}

	private String getString(Map<String, Object> map, String key) {
		return (String) Optional.ofNullable(map.get(key))
				.filter(v -> !v.toString().isEmpty())
				.orElseThrow(() -> new LicenseVerifyException(getError(key)));
	}

	private LicenseVerifyError getError(String key) {
		return switch (key) {
			case "type" -> LicenseVerifyError.EMPTY_VALID_VALUE_OPERATION;
			case "ipAddress" -> LicenseVerifyError.EMPTY_VALID_VALUE_IPADDRESS;
			case "expDate" -> LicenseVerifyError.EMPTY_VALID_VALUE_EXPIRE_DATE;
			default -> LicenseVerifyError.INVALID_LICENSE;
		};
	}
}