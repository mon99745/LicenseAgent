package com.licenseissuer.model.dto;

import com.licenseissuer.model.LicenseType;
import lombok.Builder;
import lombok.Data;

/**
 * 운영(Prod) 라이센스 정보
 */
@Data
@Builder
public class ProdLicenseDto {
	protected LicenseType type;
	protected String projectName;
	protected String ipAddress;
	protected String issuer;
}