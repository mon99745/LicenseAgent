package com.licensevalidator;

import com.jsonwebtoken.core.config.TokenAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"com.licensevalidator", "com.licensecommon"})
@Import(TokenAutoConfig.class)
public class LicenseValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseValidatorApplication.class, args);
	}

}
