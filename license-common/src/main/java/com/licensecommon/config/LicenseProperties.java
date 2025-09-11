package com.licensecommon.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 라이센스 설정
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LicenseProperties {
	public static final String PROPERTY_PREFIX = "license";
}
