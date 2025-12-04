package com.licensewebadmin.config;

import com.licensecommon.config.LicenseProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.licensecommon.config.LicenseProperties.PROPERTY_PREFIX;

/**
 * 테스트 시만 사용하는 라이선스 검증 설정
 */
@Data
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ConfigurationProperties(prefix = PROPERTY_PREFIX)
public class LicenseVerifyProperties
		extends LicenseProperties {
}