package com.licenseissuer.model.dto;

import com.licenseissuer.model.LicenseType;
import lombok.Builder;
import lombok.Data;

/**
 * 개발(Dev) 라이센스 정보
 */
@Data
@Builder
public class DevLicenseDto {
	protected LicenseType type;
	protected String projectName;
	protected String expDate;
	protected String issuer;
}