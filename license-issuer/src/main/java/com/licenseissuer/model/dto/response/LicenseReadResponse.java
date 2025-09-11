package com.licenseissuer.model.dto.response;

import com.licenseissuer.model.entity.LicenseInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 라이센스 조회 결과
 */
@Data
@Builder
public class LicenseReadResponse {
	protected int resultCode;
	protected String resultMsg;
	private List<LicenseInfo> licenses;
}