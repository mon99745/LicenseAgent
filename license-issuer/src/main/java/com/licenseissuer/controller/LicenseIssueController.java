package com.licenseissuer.controller;

import com.licenseissuer.model.dto.LicenseIssueRequest;
import com.licenseissuer.model.dto.LicenseReadRequest;
import com.licenseissuer.model.dto.LicenseReadResponse;
import com.licenseissuer.model.dto.LicenseUpdateRequest;
import com.licenseissuer.model.dto.LicenseUpdateResponse;
import com.licenseissuer.model.entity.LicenseInfo;
import com.licenseissuer.service.LicenseIssueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
		// 01. issueRequest 유효성 검증
		issueRequest.validate();

		// 02. IP 추출 및 요청 정보 바인딩
		issueRequest.setIssuerIp(httpRequest.getRemoteAddr());

		// 03. 라이센스 생성 및 발급
		ResponseEntity<Resource> resource = issueService.getLicense(issueRequest);

		// 04. 라이센스 반환
		return resource;
	}

	/**
	 * 라이센스 발급 이력 조회
	 *
	 * @param readRequest
	 * @return
	 */
	@PostMapping("history")
	public LicenseReadResponse getIssueHistory(@RequestBody LicenseReadRequest readRequest) {
		// 01. issueRequest 유효성 검증
		readRequest.validate();

		// 02. 라이센스 리스트 조회
		List<LicenseInfo> licenseInfoList = issueService.getIssueHistory(readRequest);

		// 03-1 조회된 라이센스가 없는 경우
		if (licenseInfoList.isEmpty()) {
			return LicenseReadResponse.builder()
					.resultCode(HttpStatus.OK.value())
					.resultMsg(HttpStatus.NOT_FOUND.getReasonPhrase())
					.build();
		}

		// 03-2. 응답 메시지 및 라이센스 정보 반환
		return LicenseReadResponse.builder()
				.resultCode(HttpStatus.OK.value())
				.resultMsg(HttpStatus.OK.getReasonPhrase())
				.licenses(licenseInfoList)
				.build();
	}

	/**
	 * 라이센스 발급 정보 변경
	 *
	 * @param updateRequest
	 * @return
	 */
	@PostMapping("update")
	public LicenseUpdateResponse updateIssueHistory(HttpServletRequest httpRequest,
													@RequestBody LicenseUpdateRequest updateRequest) {
		// 01. issueRequest 유효성 검증
		updateRequest.validate();

		// 02. IP 추출 및 요청 정보 바인딩
		updateRequest.setProcessorIp(httpRequest.getRemoteAddr());

		// 03. 라이센스 정보 변경
		issueService.updateIssueHistory(updateRequest);

		// 04. 응답 메시지 반환
		return LicenseUpdateResponse.builder()
				.resultCode(HttpStatus.OK.value())
				.resultMsg(HttpStatus.OK.getReasonPhrase())
				.build();
	}
}