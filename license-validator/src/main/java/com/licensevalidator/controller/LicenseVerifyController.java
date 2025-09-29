package com.licensevalidator.controller;

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


	@PostMapping("verify")
	public LicenseVerifyResponse verify(@RequestParam("filePath") String filePath) {
		if (filePath == null || filePath.isBlank()) {
			throw new IllegalArgumentException("filePath는 필수입니다.");
		}

		return verifyService.verify(filePath);
	}
}
