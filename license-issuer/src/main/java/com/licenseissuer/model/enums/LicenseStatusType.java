package com.licenseissuer.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LicenseStatusType {
	/**
	 * 활성화 상태
	 */
	ACTIVE("active"),

	/**
	 * 비활성화 상태
	 * - 재발급 불가능 상태
	 * - 기존 라이센스 자체를 삭제/회수하지 않음
	 * - 이미 사용 중인 라이센스에는 영향 없음
	 */
	DEACTIVE("deactive");

	private final String value;

	public static LicenseStatusType fromValue(String value) {
		for (LicenseStatusType type : values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid license Status type: " + value);
	}
}