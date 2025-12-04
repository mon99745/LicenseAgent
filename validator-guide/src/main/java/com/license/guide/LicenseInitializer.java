package com.license.guide;

import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.service.LicenseVerifyService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LicenseInitializer {
	/**
	 * 라이센스 검증 파일 경로
	 * - 예시: "./lic/test.lic"
	 */
	private final String LICENSE_PATH = "./lic/test_fail.lic";
//	private final String LICENSE_PATH = "./lic/test_success.lic";

	/**
	 * 라이센스 검증 서비스
	 */
	private final LicenseVerifyService verifyService;

	/**
	 * 생성자 주입
	 *
	 * @param verifyService 라이센스 검증 서비스
	 */
	public LicenseInitializer(LicenseVerifyService verifyService) {
		this.verifyService = verifyService;
	}

	/**
	 * 애플리케이션 시작 시 라이센스 파일 검증
	 */
	@PostConstruct
	public void checkLicense() {
		/**
		 * 라이센스 검증 응답메시지
		 * - getResultCode(): 결과코드
		 * - getResultMsg(): 결과메시지
		 * - isValid(): 라이센스 유효여부
		 */
		LicenseVerifyResponse response = verifyService.verify(LICENSE_PATH);

		if (response.isValid()) {
			System.out.println("라이센스 유효함");
			/**
			 * 라이센스 유효 시 애플리케이션 구동
			 */
		} else {
			System.err.println("라이센스 유효하지 않음: " + response.getResultMsg());
			/**
			 * 라이센스 유효하지 않음 시 애플리케이션 종료
			 */
			System.exit(1);
		}
	}
}