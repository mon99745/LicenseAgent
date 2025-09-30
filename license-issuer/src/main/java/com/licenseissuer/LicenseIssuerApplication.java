package com.licenseissuer;

import com.jsonwebtoken.core.config.TokenAutoConfig;
import com.licensecommon.config.SpringDocProperties;
import com.licenseissuer.model.entity.LicenseInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SpringDocProperties.class)
@SpringBootApplication(scanBasePackages = {"com.licenseissuer", "com.licensecommon"})
@EntityScan(basePackageClasses = LicenseInfo.class)
@Import(TokenAutoConfig.class)
public class LicenseIssuerApplication {
	public static void main(String[] args) {
		SpringApplication.run(LicenseIssuerApplication.class, args);
	}
}