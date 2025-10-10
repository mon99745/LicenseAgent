package com.licenseissuer.service;

import com.licenseissuer.config.LicenseIssueProperties;
import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import com.licensecommon.enums.LicenseProcessType;
import com.licensecommon.enums.LicenseStatusType;
import com.licensecommon.enums.LicenseType;
import com.licenseissuer.model.dto.request.LicenseIssueRequest;
import com.licenseissuer.model.dto.request.LicenseReadRequest;
import com.licenseissuer.model.dto.request.LicenseUpdateRequest;
import com.licenseissuer.model.dto.ProdLicenseDto;
import com.licenseissuer.model.dto.DevLicenseDto;
import com.licenseissuer.model.dto.TempLicenseDto;
import com.licenseissuer.model.entity.LicenseInfo;
import com.licenseissuer.repository.LicenseInfoRepository;
import com.licensecommon.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 라이센스 발급 서비스
 * - 라이선스를 실제 사용자/서버에 발급
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseIssueService {
	protected final LicenseIssueProperties issueProperties;
	protected final LicenseGenerateService generateService;
	protected final LicenseLogService logService;
	private final LicenseInfoRepository infoRepository;


	/**
	 * 라이센스 발급
	 *
	 * @param issueRequest 발급 요청문
	 * @return
	 */
	@Transactional
	public ResponseEntity<Resource> getLicense(LicenseIssueRequest issueRequest) {
		ResponseEntity<Resource> resource = null;
		LicenseType licType = LicenseType.fromValue(issueRequest.getOperation());
		// 01. 라이센스 생성
		switch (licType) {
			case PRODLICENSE:
				resource = generateService.createResource(ProdLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.ipAddress(issueRequest.getIpAddress())
						.build());
				break;
			case DEVLICENSE:
				resource = generateService.createResource(DevLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.expDate(issueRequest.getExpDate())
						.build());
				break;
			case TEMPLICENSE:
				resource = generateService.createResource(TempLicenseDto.builder()
						.type(licType)
						.projectName(issueRequest.getProjectName())
						.expDate(issueRequest.getExpDate())
						.build());
				break;
		}

		// 02. 라이센스 파일로 저장
		logService.saveLicenseFile(resource.getBody());

		// 03. 라이센스 발급 정보 저장
		logService.saveLicenseInfo(issueRequest);

		// 04. 라이센스 파일 반환
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
		List<LicenseInfo> licenseInfoList = infoRepository.findByIssuer(readRequest.getIssuer());
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
	 * 라이센스 발급 정보 변경
	 * - 1단계 기존 라이센스의 무효화 (Deactive)
	 * - 2단계 업데이트 정보의 신규 라이센스를 반환
	 *
	 * @param updateRequest
	 */
	@Transactional
	public void updateIssueHistory(LicenseUpdateRequest updateRequest) {
		// 01. 업데이트 대상 라이센스 조회 및 유효성 검증
		LicenseInfo licenseInfo = infoRepository.findById(updateRequest.getLicenseId())
				.orElseThrow(() -> new LicenseIssuerException(LicenseIssuerError.LICENSE_NOT_FOUND));


		// 02. 기존 라이센스의 무효화 (Deactive)
		licenseInfo.deactivate();
		log.debug("license={}", licenseInfo);
		infoRepository.save(licenseInfo);

		// 02-1. 라이센스 이력 정보 저장
		logService.saveLicenseInfoLog(licenseInfo, updateRequest.getProcessor(),
				updateRequest.getProcessorIp(), LicenseStatusType.DEACTIVE.getValue());

		// 03. 신규 라이센스를 저장
		LicenseInfo updatelicenseInfo = new LicenseInfo(
				LicenseType.fromValue(updateRequest.getOperation()),
				LicenseStatusType.ACTIVE,
				updateRequest.getProjectName(),
				updateRequest.getIpAddress(),
				DateUtil.parse(updateRequest.getExpDate()),
				updateRequest.getIssuer(),
				licenseInfo.getIssuerIp()
		);

		log.debug("update_license={}", updatelicenseInfo);
		infoRepository.save(updatelicenseInfo);

		// 03-1. 라이센스 이력 정보 저장
		logService.saveLicenseInfoLog(updatelicenseInfo, updateRequest.getProcessor(),
				updateRequest.getProcessorIp(), LicenseProcessType.REISSUE.getValue());
	}
}