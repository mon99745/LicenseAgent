package com.licenseissuer;

import com.licenseissuer.model.entity.LicenseInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = LicenseInfo.class)
public class LicenseIssuerApplication {
	public static void main(String[] args) {
		SpringApplication.run(LicenseIssuerApplication.class, args);
	}
}