package com.licensevalidator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licensevalidator.exception.LicenseVerifyError;
import com.licensevalidator.exception.LicenseVerifyException;
import io.jwt4j.lite.core.model.dto.reponse.ExtractClaimResponse;
import io.jwt4j.lite.core.model.dto.reponse.VerifyTokenResponse;
import io.jwt4j.lite.core.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 토큰 관련 모든 검사
 * + Claim 추출 포함
 */
@Service
@RequiredArgsConstructor
public class VerifyTokenService {
	private final TokenService tokenService;

	public void verify(String token) {
		VerifyTokenResponse res = tokenService.verifyJwt(token);

		if (!HttpStatus.OK.toString().equals(res.getResultCode())) {
			throw new LicenseVerifyException(
					LicenseVerifyError.FAILED_LICENSE_FORGERY_VERIFY,
					res.getResultMsg()
			);
		}
	}

	public Map<String, Object> extractClaims(String token) {
		ExtractClaimResponse res = tokenService.extractClaimToJwt(token);

		if (!HttpStatus.OK.toString().equals(res.getResultCode())) {
			throw new LicenseVerifyException(
					LicenseVerifyError.FAILED_LICENSE_DATA_EXTRACT,
					res.getResultMsg()
			);
		}

		return extractPublicClaims(res.getClaims());
	}

	private Map<String, Object> extractPublicClaims(Object claimsObj) {
		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> claimsMap = mapper.convertValue(claimsObj, Map.class);
		Map<String, Object> publicClaimsWrapper =
				(Map<String, Object>) claimsMap.get("publicClaims");

		return (Map<String, Object>) publicClaimsWrapper.get("publicClaim");
	}
}