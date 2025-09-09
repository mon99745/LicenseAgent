package com.licenseissuer.model.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 라이센스 업데이트 요청 정보
 */
@Slf4j
@Getter
public class LicenseUpdateRequest {
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


	public void validate() {
		if (operation == null || operation.isBlank()) {
			throw new IllegalArgumentException("Operation is required");
		}
		if (projectName == null || projectName.isBlank()) {
			throw new IllegalArgumentException("Project name is required");
		}
		if (issuer == null || issuer.isBlank()) {
			throw new IllegalArgumentException("Issuer is required");
		}
		if ((ipAddress == null || ipAddress.isBlank()) & (expDate == null || expDate.isBlank())) {
			throw new IllegalArgumentException("IP address and Expire Date is required");
		}
	}
}