package com.licensewebadmin;

import com.licensecommon.config.SpringDocProperties;
import io.jwt4j.lite.core.config.TokenAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SpringDocProperties.class)
@SpringBootApplication(scanBasePackages = {"com.licensewebadmin", "com.licensevalidator", "com.licensecommon"})
@Import(TokenAutoConfig.class)
public class LicenseWebadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseWebadminApplication.class, args);
	}

}
