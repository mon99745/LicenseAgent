package com.licensecommon.enums;

import com.licensecommon.exception.CommonError;
import com.licensecommon.exception.CommonException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LicenseProcessType {
	/**
	 * 라이센스 최초 발급
	 */
	ISSUE("issue"),

	/**
	 * 라이센스 재발급 (재다운로드 포함)
	 */
	REISSUE("reissue");

	private final String value;

	public static LicenseProcessType fromValue(String value) {
		for (LicenseProcessType type : values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new CommonException(CommonError.INVALID_FILE_PROCESS_TYPE, value);
	}
}