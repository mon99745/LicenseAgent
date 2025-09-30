package com.licensevalidator;

import com.jsonwebtoken.core.config.TokenAutoConfig;
import com.licensecommon.config.SpringDocProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SpringDocProperties.class)
@SpringBootApplication(scanBasePackages = {"com.licensevalidator", "com.licensecommon"})
@Import(TokenAutoConfig.class)
public class LicenseValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseValidatorApplication.class, args);
	}

}
