package com.licenseissuer.model;

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
	PRODLICENSE("prod"),

	/**
	 * 개발 라이센스
	 * (택1) 3개월, 6개월, 12개월, 24개월
	 */
	DEVLICENSE("dev"),

	/**
	 * 임시 라이센스
	 * 만료기간 선택불가 (기본 7일)
	 */
	TEMPLICENSE("temp");

	private final String value;
}