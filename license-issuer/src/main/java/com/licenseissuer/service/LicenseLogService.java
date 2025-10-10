package com.licenseissuer.service;

import com.licensecommon.enums.LicenseProcessType;
import com.licensecommon.enums.LicenseStatusType;
import com.licensecommon.enums.LicenseType;
import com.licensecommon.util.DateUtil;
import com.licenseissuer.config.LicenseIssueProperties;
import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import com.licenseissuer.model.dto.request.LicenseIssueRequest;
import com.licenseissuer.model.entity.LicenseInfo;
import com.licenseissuer.model.entity.LicenseInfoLog;
import com.licenseissuer.repository.LicenseInfoLogRepository;
import com.licenseissuer.repository.LicenseInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * 라이센스 기록 서비스
 * - 발급 내역 기록 및 감사 로그 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseLogService {
	protected final LicenseIssueProperties issueProperties;
	private final LicenseInfoRepository infoRepository;
	private final LicenseInfoLogRepository infoLogRepository;

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
			// 01. 파일 이름 생성
			String fileName = issueProperties.getLicensePrefix() + issueProperties.getLicenseSuffix();

			// 02. 디렉터리 유무에 따라 생성
			File destination = new File(issueProperties.getSavePath(), fileName);
			File parentDir = destination.toPath().getParent().toFile();
			if (!parentDir.exists()) {
				if (!parentDir.mkdirs()) {
					throw new LicenseIssuerException(LicenseIssuerError.FAIL_CREATE_DIRECTORY, parentDir.getPath());
				}
			} else if (!parentDir.isDirectory()) {
				log.error("Exists but is not a directory: {}", parentDir.getPath());
				throw new LicenseIssuerException(LicenseIssuerError.FAIL_CREATE_DIRECTORY, parentDir.getPath());
			}
			// 03. Resource → 파일 복사
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

		infoRepository.save(licenseInfo);

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
	public void saveLicenseInfoLog(LicenseInfo license, String processor, String processorIp, String prcsContent) {
		// 01. 라이센스 이력 정보 저장
		LicenseInfoLog licenseAfterInfoLog = new LicenseInfoLog(license, processor, processorIp, prcsContent);

		infoLogRepository.save(licenseAfterInfoLog);
	}
}
