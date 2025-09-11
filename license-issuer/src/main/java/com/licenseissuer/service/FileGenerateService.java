package com.licenseissuer.service;

import com.licenseissuer.config.LicenseIssueProperties;
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

@Service
@AllArgsConstructor
public class FileGenerateService {
	protected final LicenseIssueProperties issueProperties;

	/**
	 * 파일 생성
	 *
	 * @param content
	 * @param type
	 * @param projectName
	 * @return
	 */
	public ResponseEntity<Resource> createFile(String content, String type, String projectName) {
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