package com.licenseissuer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licenseissuer.config.LicenseIssueProperties;
import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import com.licenseissuer.model.enums.LicenseProcessType;
import com.licenseissuer.model.enums.LicenseStatusType;
import com.licenseissuer.model.enums.LicenseType;
import com.licenseissuer.model.dto.request.LicenseIssueRequest;
import com.licenseissuer.model.dto.request.LicenseReadRequest;
import com.licenseissuer.model.dto.request.LicenseUpdateRequest;
import com.licenseissuer.model.dto.ProdLicenseDto;
import com.licenseissuer.model.dto.DevLicenseDto;
import com.licenseissuer.model.dto.TempLicenseDto;
import com.licenseissuer.model.entity.LicenseInfo;
import com.licenseissuer.model.entity.LicenseInfoLog;
import com.licenseissuer.repository.LicenseInfoLogRepository;
import com.licenseissuer.repository.LicenseInfoRepository;
import com.licenseissuer.util.DateUtil;
import com.security.jsonwebtoken.message.CreateTokenResponse;
import com.security.jsonwebtoken.service.TokenSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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


@Slf4j
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
	public List<LicenseInfo> getIssueHistory(LicenseReadRequest readRequest) {
		// 01. 라이센스 리스트 조회
		List<LicenseInfo> licenseInfoList = licenseInfoRepository.findByProjectNameAndIssuer(
				readRequest.getProjectName(),
				readRequest.getIssuer());

		log.info("조회된 라이센스 수: {}건", licenseInfoList.size());
		licenseInfoList.forEach(license ->
				log.debug("LicenseInfo: id={}, type={}, projectName={}, issuer={}, ip={}",
						license.getId(),
						license.getType(),
						license.getProjectName(),
						license.getIssuer(),
						license.getIpAddress())
		);

		// 02. 라이센스 리스트 반환
		return licenseInfoList;
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
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_RESOURCE);
		}

		try {
			File dir = new File(licenseProperties.getSavePath());
			if (!dir.exists() && !dir.mkdirs()) {
				throw new LicenseIssuerException(LicenseIssuerError.FAIL_CREATE_DIRECTORY, "licenseProperties.getSavePath()");
			}

			String fileName = licenseProperties.getLicensePrefix()
					+ "." + licenseProperties.getLicenseSuffix();
			File destination = new File(dir, fileName);

			// Resource → 파일 복사
			try (InputStream in = resource.getInputStream()) {
				Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			throw new LicenseIssuerException(LicenseIssuerError.FAIL_SAVE_LICENSE, e);
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
				LicenseStatusType.ACTIVE,
				issueRequest.getProjectName(),
				issueRequest.getIpAddress(),
				expDate,
				issueRequest.getIssuer(),
				issueRequest.getIssuerIp()
		);

		licenseInfoRepository.save(licenseInfo);

		// 02. 라이센스 이력 정보 저장
		saveLicenseInfoLog(licenseInfo, issueRequest.getIssuer(),
				issueRequest.getIssuerIp(), LicenseProcessType.ISSUE.getValue());
	}


	/**
	 * 라이센스 이력 정보 저장
	 *
	 * @param license     라이센스
	 * @param processor   처리자
	 * @param processorIp 처리자 IP
	 * @param prcsContent 처리내용
	 */
	private void saveLicenseInfoLog(LicenseInfo license, String processor, String processorIp, String prcsContent) {
		// 01. 라이센스 이력 정보 저장
		LicenseInfoLog licenseAfterInfoLog = new LicenseInfoLog(license, processor, processorIp, prcsContent);
		licenseInfoLogRepository.save(licenseAfterInfoLog);
	}

	/**
	 * 라이센스 발급 정보 변경
	 * - 1단계 기존 라이센스의 무효화 (Deactive)
	 * - 2단계 업데이트 정보의 신규 라이센스를 반환
	 *
	 * @param updateRequest
	 */
	@Transactional
	public void updateIssueHistory(LicenseUpdateRequest updateRequest) {
		LicenseReadRequest readInfo = new LicenseReadRequest();
		LicenseInfo licenseInfo;
		Date updateExpDate = DateUtil.parse(updateRequest.getExpDate());

		// 01. 업데이트 대상 라이센스 조회 및 유효성 검증
		readInfo.setReadInfo(updateRequest.getProjectName(),
				updateRequest.getIssuer());
		List<LicenseInfo> licenseInfoList = getIssueHistory(readInfo);
		if (licenseInfoList.size() != 1 || licenseInfoList.isEmpty()) {
			throw new LicenseIssuerException(LicenseIssuerError.FAIL_UPDATE_QUERY_LICENSE, updateRequest.getIssuer());
		} else {
			licenseInfo = licenseInfoList.get(0);
			log.info("before={}", licenseInfo);

		}

		// 02. 기존 라이센스의 무효화 (Deactive)
		licenseInfo.deactivate();
		licenseInfoRepository.save(licenseInfo);

		// 03. 신규 라이센스를 저장
		LicenseInfo updatelicenseInfo = new LicenseInfo(
				LicenseType.fromValue(updateRequest.getOperation()),
				LicenseStatusType.ACTIVE,
				updateRequest.getProjectName(),
				updateRequest.getIpAddress(),
				updateExpDate,
				updateRequest.getIssuer(),
				licenseInfo.getIssuerIp()
		);

		log.debug("after={}", updatelicenseInfo);
		licenseInfoRepository.save(updatelicenseInfo);

		// 04-1. 라이센스 이력 정보 저장
		saveLicenseInfoLog(licenseInfo, updateRequest.getProcessor(),
				updateRequest.getProcessorIp(), LicenseStatusType.DEACTIVE.getValue());

		// 04-2. 라이센스 이력 정보 저장
		saveLicenseInfoLog(updatelicenseInfo, updateRequest.getProcessor(),
				updateRequest.getProcessorIp(), LicenseProcessType.REISSUE.getValue());
	}
}