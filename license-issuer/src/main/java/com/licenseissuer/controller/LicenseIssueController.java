package com.licenseissuer.controller;

import com.licenseissuer.model.dto.LicenseIssueRequest;
import com.licenseissuer.model.dto.LicenseReadResponse;
import com.licenseissuer.service.LicenseIssueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping(LicenseIssueController.PATH)
public class LicenseIssueController {
	public static final String PATH = "/api/license/issue";
	protected final LicenseIssueService issueService;

	/**
	 * 라이센스 발급 및 다운로드
	 *
	 * @param httpRequest
	 * @param issueRequest
	 * @return
	 */
	@PostMapping("download")
	public ResponseEntity<Resource> getLicense(HttpServletRequest httpRequest,
											   @RequestBody LicenseIssueRequest issueRequest) {
		// 01. issueRequest 유효 검증
		issueRequest.validate();

		// 02. IP 추출 및 요청 정보 바인딩
		issueRequest.setIssuerIp(httpRequest.getRemoteAddr());

		// 03. 서비스 레이어로 전달 및 반환
		return issueService.getLicense(issueRequest);
	}

	/**
	 * 라이센스 발급 이력
	 *
	 * @param httpRequest
	 * @param issueRequest
	 * @return
	 */
	@PostMapping("history")
	public LicenseReadResponse getIssueHistory(HttpServletRequest httpRequest,
											   @RequestBody LicenseIssueRequest issueRequest) {
		// 01. 로그(info) - IP 추출 및 요청 정보 바인딩
		// 02. 서비스 레이어로 전달 및 반환
		return LicenseReadResponse.builder().build();
	}
}