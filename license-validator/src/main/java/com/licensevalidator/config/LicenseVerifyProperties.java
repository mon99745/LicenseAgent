package com.licensevalidator.config;

import com.licensecommon.config.LicenseProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.licensecommon.config.LicenseProperties.PROPERTY_PREFIX;

@Data
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ConfigurationProperties(prefix = PROPERTY_PREFIX)
public class LicenseVerifyProperties
		extends LicenseProperties {
}