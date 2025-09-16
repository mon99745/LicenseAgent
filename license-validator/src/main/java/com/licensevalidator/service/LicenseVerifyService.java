package com.licensevalidator.service;

import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.util.FileUtil;
import com.security.jsonwebtoken.model.dto.reponse.VerifyTokenResponse;
import com.security.jsonwebtoken.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseVerifyService {
	protected final TokenService tokenService;

	/**
	 * 라이센스 검증
	 *
	 * @param file 라이센스 파일
	 * @return
	 */
	public LicenseVerifyResponse verify(File file) {
		// 01. 파일 내 토큰 값 추출
		String token = FileUtil.readFileContent(file);

		// 02. 토큰 진위 검증
		VerifyTokenResponse verifyTokenResponse = tokenService.verifyJwt(token);
		String verifyResultCode = verifyTokenResponse.getResultCode();
		String verifyResultMsg = verifyTokenResponse.getResultCode();
		log.info("[JWT Verify] Result_Code={}, Result_Msg={}", verifyResultCode, verifyResultMsg);

//		// 03. 라이센스 정보 추출
//		ExtractClaimResponse extractClaimResponse = tokenService.extractClaimToJwt(token);
//		String extractResultCode = extractClaimResponse.getResultCode();
//		String extractResultMsg = extractClaimResponse.getResultCode();
//		log.info("[JWT Extract] Result_Code={}, Result_Msg={}", extractResultCode, extractResultMsg);

		// 04. 라이센스 정보 유효성 검증
		// 05. 결과 반환
		if (verifyResultCode.equals(String.valueOf(HttpStatus.OK.value())) && verifyResultMsg.equals("Success")) {
			return LicenseVerifyResponse.builder()
					.isValid(true)
					.resultCode(verifyResultCode)
					.resultMsg(verifyResultMsg)
					.build();
		} else {
			return LicenseVerifyResponse.builder()
					.isValid(false)
					.resultCode(verifyResultCode)
					.resultMsg(verifyResultMsg)
					.build();
		}
	}
}
