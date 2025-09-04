package com.licenseissuer.service;

import com.licenseissuer.model.dto.LicenseIssueRequest;
import com.licenseissuer.model.dto.ProdLicenseDto;
import com.licenseissuer.model.dto.DevLicenseDto;
import com.licenseissuer.model.dto.TempLicenseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;


@Service
@AllArgsConstructor
public class LicenseIssueService {
	protected final FileGenerateService fileGenerateService;

	/**
	 * 라이센스 발급
	 * @param issueRequest
	 * @return
	 */
	@Transactional
	public void getLicense(LicenseIssueRequest issueRequest) {
		// 01. 오퍼레이션 구분
			//01-1 (운영)/(개발/임시) 라이센스 생성
		// 02. 라이센스 파일 저장
		// 03. 라이센스 발급 정보 저장
		// 04. 라이센스 파일 반환
	}

	/**
	 * 라이센스 발급 이력 조회
	 * @param issueRequest
	 * @return
	 */
	public void getIssueHistory(LicenseIssueRequest issueRequest) {
		// 01. 라이센스 발급 이력 조회
		// 02. 라이센스 조회 이력 저장
	}

	/**
	 * 운영 라이센스 생성
	 * @param prodLicenseDto
	 */
	public void createLicense(ProdLicenseDto prodLicenseDto) {
		// 01. JWT 토큰생성
		// 02. 토큰 활용 파일화
		// 03. 라이센스 파일 반환

	}

	/**
	 * 개발 라이센스 생성
	 * @param devLicenseDto
	 */
	public void createLicense(DevLicenseDto devLicenseDto) {
		// 01. JWT 토큰생성
		// 02. 토큰 활용 파일화
		// 03. 라이센스 파일 반환
	}

	/**
	 * 임시 라이센스 생성
	 * @param tempLicenseDto
	 */
	public void createLicense(TempLicenseDto tempLicenseDto) {
		// 01. JWT 토큰생성
		// 02. 토큰 활용 파일화
		// 03. 라이센스 파일 반환
	}

	/**
	 * 라이센스 파일 저장 (파일 db)
	 * @param file
	 */
	public void saveLicenseFile(File file) {
		// 01. 라이센스 파일 저장 (파일 db)
	}

	/**
	 * 라이센스 발급 정보 저장
	 * @param issueRequest
	 */
	public void saveLicenseInfo(LicenseIssueRequest issueRequest) {
		// 01. 라이센스 발급 정보 저장
	}
}