package com.licensevalidator.model.response;

import lombok.Builder;
import lombok.Data;


/**
 * 라이센스 검증 결과
 */
@Data
@Builder
public class LicenseVerifyResponse {
	private String resultCode;
	private String resultMsg;
	protected boolean isValid;
}
