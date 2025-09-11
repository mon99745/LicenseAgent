package com.licenseissuer.model.dto.request;

import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 라이센스 발급 요청 정보
 */
@Slf4j
@Getter
public class LicenseIssueRequest {
	protected String operation;
	protected String projectName;
	protected String ipAddress;
	protected String expDate;
	protected String issuer;
	protected String issuerIp;

	public void setIssuerIp(String issuerIp) {
		this.issuerIp = issuerIp;
	}


	public void validate() {
		if (operation == null || operation.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_OPERATION);
		}
		if (projectName == null || projectName.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_PROJECTNAME);
		}
		if (issuer == null || issuer.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_ISSUER);
		}
		if ((ipAddress == null || ipAddress.isBlank())
				& (expDate == null || expDate.isBlank())) {
			throw new LicenseIssuerException(LicenseIssuerError.FAIL_UPDATE_QUERY_LICENSE);
		}
	}
}