package com.licensevalidator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licensecommon.enums.LicenseType;
import com.jsonwebtoken.core.model.dto.reponse.ExtractClaimResponse;
import com.jsonwebtoken.core.model.dto.reponse.VerifyTokenResponse;
import com.jsonwebtoken.core.service.TokenService;
import com.licensecommon.util.DateUtil;
import com.licensevalidator.model.response.LicenseVerifyResponse;
import com.licensevalidator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseVerifyService {
	protected final TokenService tokenService;

	/**
	 * 라이센스 검증
	 *
	 * @param licPath 라이센스 경로
	 * @return
	 */
	public LicenseVerifyResponse verify(String licPath) {
		try {

			File lic = new File(licPath);

			// 01. 라이센스 유효성 검사
			FileUtil.validateFile(lic);

			// 02. 라이센스 리소스 추출
			String token = FileUtil.readFileContent(lic);

			// 03. 리소스 위변조 검증
			verifyToken(token);

			// 04. 리소스 데이터 항목 추출
			Map<String, Object> claims = extractResource(token);

			// 05. 리소스 데이터 항목 검증
			verifyResource(claims);

			return LicenseVerifyResponse.builder()
					.resultCode(String.valueOf(HttpStatus.OK.value()))
					.resultMsg("라이센스 검증 성공")
					.isValid(true)
					.build();
		} catch (IllegalArgumentException | IllegalStateException e) {
			log.error("라이센스 검증 실패: {}", e.getMessage());
			return LicenseVerifyResponse.builder()
					.resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
					.resultMsg(e.getMessage())
					.isValid(false)
					.build();
		} catch (Exception e) {
			log.error("예상치 못한 오류 발생", e);
			return LicenseVerifyResponse.builder()
					.resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
					.resultMsg("시스템 오류 발생")
					.isValid(false)
					.build();
		}
	}

	/**
	 * 리소스 위변조 검증
	 *
	 * @param token
	 */
	public void verifyToken(String token) {
		VerifyTokenResponse verifyTokenResponse = tokenService.verifyJwt(token);
		String verifyResultCode = verifyTokenResponse.getResultCode();
		String verifyResultMsg = verifyTokenResponse.getResultMsg();
		log.info("[JWT Verify] Result_Code={}, Result_Msg={}", verifyResultCode, verifyResultMsg);

		if (!String.valueOf(HttpStatus.OK.value()).equals(verifyResultCode)) {
			throw new IllegalArgumentException("JWT 위변조 검증 실패: " + verifyResultMsg);
		}
	}

	/**
	 * 리소스 데이터 항목 추출
	 *
	 * @param token
	 * @return
	 */
	public Map<String, Object> extractResource(String token) {
		ExtractClaimResponse extractClaimResponse = tokenService.extractClaimToJwt(token);
		String extractResultCode = extractClaimResponse.getResultCode();
		String extractResultMsg = extractClaimResponse.getResultMsg();
		log.info("[JWT Extract] Result_Code={}, Result_Msg={}", extractResultCode, extractResultMsg);

		if (!String.valueOf(HttpStatus.OK.value()).equals(extractResultCode)) {
			throw new IllegalArgumentException("JWT 데이터 추출 실패: " + extractResultMsg);
		}

		Map<String, Object> publicClaimsMap = new HashMap<>();

		Object claimsObj = extractClaimResponse.getClaims();
		if (claimsObj != null) {
			ObjectMapper mapper = new ObjectMapper();
			// Object -> Map 변환
			Map<String, Object> claimsMap = mapper.convertValue(claimsObj, Map.class);

			Object publicClaimsWrapper = claimsMap.get("publicClaims");
			if (publicClaimsWrapper instanceof Map<?, ?> wrapperMap) {
				Object publicClaim = wrapperMap.get("publicClaim");
				if (publicClaim instanceof Map<?, ?> claimMap) {
					publicClaimsMap.putAll((Map<String, Object>) claimMap);
				}
			}
		}

		return publicClaimsMap;
	}

	/**
	 * 리소스 데이터 항목 검증
	 *
	 * @param claims
	 */
	public void verifyResource(Map<String, Object> claims) {
		String operation = (String) Optional.ofNullable(claims.get("type"))
				.filter(v -> !v.toString().isEmpty())
				.orElseThrow(() -> new IllegalArgumentException("operation is null"));

		// 01. 라이센스 구분 및 검증
		LicenseType licKey = LicenseType.fromKey(operation);
		switch (licKey) {
			case PRODLICENSE:
				String ipAddress = (String) Optional.ofNullable(claims.get("ipAddress"))
						.filter(v -> !v.toString().isEmpty())
						.orElseThrow(() -> new IllegalArgumentException("ipAddress is null"));
				try {
					String localAddress = InetAddress.getLocalHost().getHostAddress();
					if (!ipAddress.equals(localAddress)) {
						throw new IllegalArgumentException("라이센스가 유효하지 않습니다.");
					}
				} catch (UnknownHostException e) {
					throw new IllegalArgumentException("로컬 IP 조회 실패");
				}
				break;
			case DEVLICENSE, TEMPLICENSE:
				Date current = new Date();
				String expDate = (String) Optional.ofNullable(claims.get("expDate"))
						.filter(v -> !v.toString().isEmpty())
						.orElseThrow(() -> new IllegalArgumentException("expDate is null"));

				if (current.after(DateUtil.parse(expDate))) {
					throw new IllegalStateException("The license has expired (" + expDate + ")");
				}
				break;
			default:
				throw new IllegalArgumentException("지원하지 않는 라이센스 타입: " + operation);
		}
	}
}