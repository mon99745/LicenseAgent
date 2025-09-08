package com.licenseissuer.config;

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
public class LicenseIssueProperties {
	public static final String PROPERTY_PREFIX = "lic";

	protected String licenseName = "demo";
	protected String licensePrefix = "";
	protected String licenseSuffix = "lic";
	protected String savePath = "./license/";
}