package com.licenseissuer.model.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * 라이센스 업데이트 결과
 */
@Data
@Builder
public class LicenseUpdateResponse {
	protected int resultCode;
	protected String resultMsg;
}