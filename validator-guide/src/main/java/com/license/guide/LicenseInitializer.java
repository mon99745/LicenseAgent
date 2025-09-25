package com.license.guide;

import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.service.LicenseVerifyService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.File;

@Component
public class LicenseInitializer {
	private final String LICENSE_PATH = "./lic/demo_prod_20250911_TestProject100.lic";

	private final LicenseVerifyService verifyService;

	public LicenseInitializer(LicenseVerifyService verifyService) {
		this.verifyService = verifyService;
	}

	/**
	 * 애플리케이션 시작 시 라이센스 파일 검증
	 */
	@PostConstruct
	public void checkLicense() {
		File licenseFile = new File(LICENSE_PATH);
		LicenseVerifyResponse response = verifyService.verify(licenseFile);

		if (response.isValid()) {
			System.out.println("라이센스 유효함");
		} else {
			System.err.println("라이센스 유효하지 않음: " + response.getResultMsg());
			System.exit(1);
		}
	}
}