package com.licenseissuer.controller;

import com.licenseissuer.model.dto.LicenseIssueRequest;
import com.licenseissuer.model.dto.LicenseReadResponse;
import com.licenseissuer.service.LicenseIssueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/license")
public class LicenseIssueController {
	protected final LicenseIssueService licenseIssueService;

	@PostMapping("issue")
	public void getLicense(HttpServletRequest httpRequest,
						   @RequestBody LicenseIssueRequest issueRequest) {
		// 01. issueRequest 유효 검증
		// 02. IP 추출 및 요청 정보 바인딩
		// 03. 서비스 레이어로 전달 및 반환
	}

	@PostMapping("issue/history")
	public LicenseReadResponse getIssueHistory(HttpServletRequest httpRequest,
											   @RequestBody LicenseIssueRequest issueRequest) {
		// 01. 로그(info) - IP 추출 및 요청 정보 바인딩
		// 02. 서비스 레이어로 전달 및 반환
		return LicenseReadResponse.builder().build();
	}
}