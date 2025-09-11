package com.licenseissuer.model.dto.request;

import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 라이센스 조회 요청 정보
 */
@Slf4j
@Getter
public class LicenseReadRequest {
	protected String operation;
	protected String projectName;
	protected String ipAddress;
	protected String expDate;
	protected String issuer;
	protected String processor;
	protected String processorIp;


	public void setProcessorIp(String processorIp) {
		this.processorIp = processorIp;
	}

	/** 검색 조건 */
	public void setReadInfo(String projectName, String issuer) {
		this.projectName = projectName;
		this.issuer = issuer;
	}

	public void validate() {
		if (issuer == null || issuer.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_READ_VALUE_ISSUER);
		}
	}
}