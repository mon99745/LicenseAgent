package com.licensevalidator.controller;

import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.service.LicenseVerifyService;
import com.licensevalidator.util.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@AllArgsConstructor
@RequestMapping(LicenseVerifyController.PATH)
public class LicenseVerifyController {
	public static final String PATH = "/api/license";
	protected final LicenseVerifyService verifyService;


	@PostMapping("verify")
	public LicenseVerifyResponse verify(@RequestParam("filePath") String filePath) {
		// 01. 파일 유효성 검증
		File file = new File(filePath);
		FileUtil.validateFile(file);

		// 02. 라이센스 검증
		LicenseVerifyResponse verifyResponse = verifyService.verify(file);

		// 03. 검증 결과 반환
		return verifyResponse;
	}
}
