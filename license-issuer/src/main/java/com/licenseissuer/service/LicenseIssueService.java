package com.licenseissuer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licenseissuer.config.LicenseIssueProperties;
import com.licenseissuer.model.LicenseType;
import com.licenseissuer.model.dto.LicenseIssueRequest;
import com.licenseissuer.model.dto.LicenseReadRequest;
import com.licenseissuer.model.dto.LicenseReadResponse;
import com.licenseissuer.model.dto.ProdLicenseDto;
import com.licenseissuer.model.dto.DevLicenseDto;
import com.licenseissuer.model.dto.TempLicenseDto;
import com.licenseissuer.model.entity.LicenseInfo;
import com.licenseissuer.repository.LicenseInfoLogRepository;
import com.licenseissuer.repository.LicenseInfoRepository;
import com.licenseissuer.util.DateUtil;
import com.security.jsonwebtoken.message.CreateTokenResponse;
import com.security.jsonwebtoken.service.TokenSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class LicenseIssueService {
	protected final LicenseIssueProperties licenseProperties;
	protected final FileGenerateService fileService;
	protected final TokenSerivce tokenSerivce;
	private final LicenseInfoRepository licenseInfoRepository;
	private final LicenseInfoLogRepository licenseInfoLogRepository;


	/**
	 * 라이센스 발급
	 *
	 * @param issueRequest 발급 요청문
	 * @return
	 */
	@Transactional
	public ResponseEntity<Resource> getLicense(LicenseIssueRequest issueRequest) {
		ResponseEntity<Resource> resource = null;

		// 01. 라이센스 구분 및 발급
		LicenseType licType = LicenseType.fromValue(issueRequest.getOperation());
		switch (licType) {
			case PRODLICENSE:
				// 02. 라이센스 생성
				resource = createLicense(ProdLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.ipAddress(issueRequest.getIpAddress())
						.build());
				break;
			case DEVLICENSE:
				// 02. 라이센스 생성
				resource = createLicense(DevLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.expDate(issueRequest.getIpAddress())
						.build());
				break;
			case TEMPLICENSE:
				// 02. 라이센스 생성
				resource = createLicense(TempLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.expDate(issueRequest.getIpAddress())
						.build());
				break;
		}

		// 03. 라이센스 파일로 저장
		saveLicenseFile(resource.getBody());

		// 04. 라이센스 발급 정보 저장
		saveLicenseInfo(issueRequest);

		// 05. 라이센스 파일 반환
		return resource;
	}

	/**
	 * 라이센스 발급 이력 조회
	 *
	 * @param readRequest
	 * @return
	 */
	@Transactional
	public LicenseReadResponse getIssueHistory(LicenseReadRequest readRequest) {
		// 01. 라이센스 발급 이력 조회
		List<LicenseInfo> licenseInfoList = licenseInfoRepository.findByProjectNameAndIssuer(
						readRequest.getProjectName(),
						readRequest.getIssuer()
				);

		if (licenseInfoList.isEmpty()) {
			return LicenseReadResponse.builder()
					.resultCode(HttpStatus.OK.value())
					.resultMsg(HttpStatus.NOT_FOUND.getReasonPhrase())
					.build();
		}

		// 03. 라이센스 발급 이력 반환
		return LicenseReadResponse.builder()
				.resultCode(HttpStatus.OK.value())
				.resultMsg(HttpStatus.OK.getReasonPhrase())
				.licenses(licenseInfoList)
				.build();
	}

	/**
	 * 운영 라이센스 생성
	 *
	 * @param prodLicenseDto 운영 라이센스 객체
	 */
	public ResponseEntity<Resource> createLicense(ProdLicenseDto prodLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> temp = objectMapper.convertValue(prodLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenSerivce.createJwt(temp);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return fileService.createFile(token, prodLicenseDto.getType().getValue(), prodLicenseDto.getProjectName());
	}

	/**
	 * 개발 라이센스 생성
	 *
	 * @param devLicenseDto 개발 라이센스 객체
	 */
	public ResponseEntity<Resource> createLicense(DevLicenseDto devLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> temp = objectMapper.convertValue(devLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenSerivce.createJwt(temp);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return fileService.createFile(token, devLicenseDto.getType().getValue(), devLicenseDto.getProjectName());
	}

	/**
	 * 임시 라이센스 생성
	 *
	 * @param tempLicenseDto 임시 라이센스 객체
	 */
	public ResponseEntity<Resource> createLicense(TempLicenseDto tempLicenseDto) {
		ObjectMapper objectMapper = new ObjectMapper();

		// 01. 토큰 생성
		Map<String, String> temp = objectMapper.convertValue(tempLicenseDto, Map.class);
		CreateTokenResponse tokenResponse = tokenSerivce.createJwt(temp);
		String token = tokenResponse.getJwt();

		// 02. 라이센스 생성 및 반환
		return fileService.createFile(token, tempLicenseDto.getType().getValue(), tempLicenseDto.getProjectName());
	}

	/**
	 * 라이센스 파일 저장 (파일 db)
	 *
	 * @param resource 라이센스 정보 포함된 리소스
	 */
	public void saveLicenseFile(Resource resource) {
		if (resource == null) {
			throw new IllegalArgumentException("Resource is null");
		}

		try {
			File dir = new File(licenseProperties.getSavePath());
			if (!dir.exists() && !dir.mkdirs()) {
				throw new RuntimeException("Failed to create directory: " + licenseProperties.getSavePath());
			}

			String fileName = licenseProperties.getLicensePrefix()
					+ "." + licenseProperties.getLicenseSuffix();
			File destination = new File(dir, fileName);

			// Resource → 파일 복사
			try (InputStream in = resource.getInputStream()) {
				Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to save license file", e);
		}
	}

	/**
	 * 라이센스 발급 정보 저장
	 *
	 * @param issueRequest
	 */
	public void saveLicenseInfo(LicenseIssueRequest issueRequest) {
		Date expDate = DateUtil.parse(issueRequest.getExpDate());

		// 01. 라이센스 발급 정보 저장
		LicenseInfo licenseInfo = new LicenseInfo(
				LicenseType.fromValue(issueRequest.getOperation()),
				issueRequest.getProjectName(),
				issueRequest.getIpAddress(),
				expDate,
				issueRequest.getIssuer(),
				issueRequest.getIssuerIp()
		);

		licenseInfoRepository.save(licenseInfo);
	}
}