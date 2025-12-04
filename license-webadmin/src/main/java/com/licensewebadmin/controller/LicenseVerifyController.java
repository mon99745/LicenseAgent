package com.licensewebadmin.controller;

import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.service.LicenseVerifyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(LicenseVerifyController.PATH)
public class LicenseVerifyController {
	public static final String PATH = "/api/license";
	protected final LicenseVerifyService verifyService;

	/**
	 * 라이센스 검증
	 *
	 * @param filePath
	 * @return
	 */
	@PostMapping("verify")
	public LicenseVerifyResponse verify(@RequestParam("filePath") String filePath) {
		if (filePath == null || filePath.isBlank()) {
			throw new LicenseVerifyException(LicenseVerifyError.EMPTY_FILE_PATH);
		}

		return verifyService.verify(filePath);
	}
}
