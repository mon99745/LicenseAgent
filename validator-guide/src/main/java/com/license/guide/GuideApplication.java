package com.license.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.license.guide", "com.licensevalidator", "com.security.jsonwebtoken"})
public class GuideApplication {
	public static void main(String[] args) {
		/**
		 * 1. 라이센스 확인
		 * @PostConstruct LicenseInitializer 에서 수행
		 */

		/**
		 * 2. 서버 실행
		 */
		SpringApplication.run(GuideApplication.class, args);
	}
}