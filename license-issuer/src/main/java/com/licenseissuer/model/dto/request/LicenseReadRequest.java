package com.licenseissuer.model.dto.request;

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

	/**
	 * 필드 중 하나라도 값이 있는지 확인
	 * @return true: 하나 이상 값 존재, false: 모두 null 또는 빈 문자열
	 */
	public boolean hasAnyValue() {
		return (operation != null && !operation.isEmpty())
				|| (projectName != null && !projectName.isEmpty())
				|| (ipAddress != null && !ipAddress.isEmpty())
				|| (expDate != null && !expDate.isEmpty())
				|| (issuer != null && !issuer.isEmpty());
	}

	/**
	 * validate 메서드 예시: 값이 하나도 없으면 예외
	 */
	public void validate() {
		if (!hasAnyValue()) {
			throw new IllegalArgumentException("At least one field value is required");
		}
	}
}