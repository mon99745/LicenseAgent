package com.licenseissuer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jwt4j.lite.core.model.dto.reponse.CreateTokenResponse;
import io.jwt4j.lite.core.service.TokenService;
import com.licenseissuer.config.LicenseIssueProperties;
import com.licenseissuer.model.dto.DevLicenseDto;
import com.licenseissuer.model.dto.ProdLicenseDto;
import com.licenseissuer.model.dto.TempLicenseDto;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 라이센스 생성 서비스
 * - 라이선스 키 자체 및 파일 생성
 */
@Service
@AllArgsConstructor
public class LicenseGenerateService {
	protected final LicenseIssueProperties issueProperties;
	protected final TokenService tokenService;

	/**
	 * 운영 라이센스 리소스 생성
	 *
	 * @param prodLicenseDto 운영 라이센스 객체
	 */
	public ResponseEntity<Resource> createResource(ProdLicenseDto prodLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> claim = objectMapper.convertValue(prodLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenService.createJwt(claim);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return createLicense(token, prodLicenseDto.getType().getValue(), prodLicenseDto.getProjectName());
	}

	/**
	 * 개발 라이센스 리소스 생성
	 *
	 * @param devLicenseDto 개발 라이센스 객체
	 */
	public ResponseEntity<Resource> createResource(DevLicenseDto devLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> claim = objectMapper.convertValue(devLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenService.createJwt(claim);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return createLicense(token, devLicenseDto.getType().getValue(), devLicenseDto.getProjectName());
	}

	/**
	 * 임시 라이센스 리소스 생성
	 *
	 * @param tempLicenseDto 임시 라이센스 객체
	 */
	public ResponseEntity<Resource> createResource(TempLicenseDto tempLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> claim = objectMapper.convertValue(tempLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenService.createJwt(claim);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return createLicense(token, tempLicenseDto.getType().getValue(), tempLicenseDto.getProjectName());
	}

	/**
	 * 라이센스 생성
	 *
	 * @param content
	 * @param type
	 * @param projectName
	 * @return
	 */
	public ResponseEntity<Resource> createLicense(String content, String type, String projectName) {
		// 01. 파일 생성 (파일명: 라이선스명_계약명_발급일_타입.lic)
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
		// 01-1 File Prefix 초기화
		issueProperties.setLicensePrefix(String.format("%s_%s_%s_%s",
				issueProperties.getLicenseName(), type.replaceAll("\\s+", ""), dateStr, projectName));
		// 01-2 File Name 초기화
		String fileName = String.format(issueProperties.getLicensePrefix() + issueProperties.getLicenseSuffix());


		// 02. 문자열을 바이트 스트림으로 변환
		byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
		ByteArrayResource resource = new ByteArrayResource(contentBytes);

		// 03. HTTP 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

		// 04. ResponseEntity로 파일 반환
		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(contentBytes.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
}