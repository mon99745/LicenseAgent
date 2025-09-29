package com.licensecommon.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LicenseType {
	/**
	 * 운영 라이센스
	 * 할당 IP 무제한
	 */
	PRODLICENSE("PRODLICENSE", "prod"),

	/**
	 * 개발 라이센스
	 * (택1) 3개월, 6개월, 12개월, 24개월
	 */
	DEVLICENSE("DEVLICENSE", "dev"),

	/**
	 * 임시 라이센스
	 * 만료기간 선택불가 (기본 7일)
	 */
	TEMPLICENSE("TEMPLICENSE", "temp");

	private final String key;
	private final String value;

	public static LicenseType fromValue(String value) {
		for (LicenseType type : values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unsupported license type:" + value);
	}
	public static LicenseType fromKey(String key) {
		for (LicenseType type : values()) {
			if (type.key.equalsIgnoreCase(key)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unsupported license key:" + key);
	}
}