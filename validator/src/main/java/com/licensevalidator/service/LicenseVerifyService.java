package com.licensevalidator.service;

import io.jwt4j.lite.core.exception.TokenException;
import com.licensevalidator.exception.LicenseVerifyException;
import com.licensevalidator.model.response.LicenseVerifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseVerifyService {
	private final VerifyFileService verifyFileService;
	private final VerifyTokenService verifyTokenService;
	private final VerifyResourceService verifyResourceService;

	/**
	 * 라이센스 검증
	 *
	 * @param licPath 라이센스 경로
	 * @return
	 */
	public LicenseVerifyResponse verify(String licPath) {
		try {

			// 01. 라이센스 유효성 검사
			File lic = verifyFileService.validate(licPath);

			// 02. 라이센스 리소스 추출
			String token = verifyFileService.readToken(lic);

			// 03. 리소스 위변조 검증
			verifyTokenService.verify(token);

			// 04. 리소스 데이터 항목 추출
			Map<String, Object> claims = verifyTokenService.extractClaims(token);

			// 05. 리소스 데이터 항목 검증
			verifyResourceService.verify(claims);

			return LicenseVerifyResponse.builder()
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.isValid(true)
					.build();
		} catch (LicenseVerifyException e) {
			log.error("라이센스 검증 실패: {}", e.getMessage());
			return LicenseVerifyResponse.builder()
					.resultCode(e.getError().getCode())
					.resultMsg(e.getMessage())
					.isValid(false)
					.build();
		} catch (TokenException e) {
			log.error("라이센스 검증 실패: {}", e.getMessage());
			return LicenseVerifyResponse.builder()
					.resultCode(e.getError().getCode())
					.resultMsg(e.getMessage())
					.isValid(false)
					.build();
		}
	}
}