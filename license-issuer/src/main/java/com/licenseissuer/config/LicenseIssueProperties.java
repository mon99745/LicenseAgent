package com.licenseissuer.config;

import com.licensecommon.config.LicenseProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.licenseissuer.config.LicenseIssueProperties.PROPERTY_PREFIX;

@Data
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ConfigurationProperties(prefix = PROPERTY_PREFIX)
public class LicenseIssueProperties
		extends LicenseProperties {

	protected String licenseName = "demo";
	protected String licensePrefix = "";
	protected String licenseSuffix = "lic";
	protected String savePath = "./license/";
}