package com.licenseissuer.model.dto;

import com.licenseissuer.model.LicenseType;
import lombok.Builder;
import lombok.Data;

/**
 * 임시(Temp) 라이센스 정보
 */
@Data
@Builder
public class TempLicenseDto {
	protected LicenseType type;
	protected String projectName;
	protected String expDate;
	protected String issuer;
}