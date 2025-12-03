package com.license.guide;

import io.jwt4j.lite.core.config.TokenAutoConfig;
import com.licensevalidator.service.LicenseVerifyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TokenAutoConfig.class, LicenseVerifyService.class})
public class GuideApplication {
	public static void main(String[] args) {
		/**
		 * 1. 라이센스 검증 (0: shutdown, 1: startup)
		 * @PostConstruct LicenseInitializer 내에서 대리 수행
		 *   - 라이센스 검증 시 라이센스 파일 경로는 LICENSE_PATH 변수에서 지정
		 *   - LICENSE_PATH 변수는 LicenseInitializer.java 에서 지정
		 *   - LICENSE_PATH 변수는 절대 경로 또는 상대 경로로 지정 가능
		 *   - 상대 경로는 애플리케이션 실행 경로를 기준으로 지정
		 */

		/**
		 * 2. 서버 구동
		 */
		SpringApplication.run(GuideApplication.class, args);
	}
}