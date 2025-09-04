package com.licenseissuer.model.dto;

import lombok.Getter;

/**
 * 라이센스 발급 요청 정보
 */
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
}