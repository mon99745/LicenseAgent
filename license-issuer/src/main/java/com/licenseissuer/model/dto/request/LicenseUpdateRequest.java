package com.licenseissuer.model.dto.request;

import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import com.licenseissuer.model.enums.LicenseType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 라이센스 업데이트 요청 정보
 */
@Slf4j
@Getter
public class LicenseUpdateRequest {
	protected Long licenseId;
	protected String operation;
	protected String projectName;
	protected String ipAddress;
	protected String expDate;
	protected String issuer;

	protected String processor;
	protected String processorIp;

	public void validate() {
		if (licenseId == null) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_UPDATE_VALUE_LICENSEID);
		}
		if (operation == null || operation.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_UPDATE_VALUE_OPERATION);
		}
		if (projectName == null || projectName.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_UPDATE_VALUE_PROJECTNAME);
		}
		if (issuer == null || issuer.isBlank()) {
			throw new LicenseIssuerException(LicenseIssuerError.EMPTY_UPDATE_VALUE_ISSUER);
		}

		final LicenseType licenseType;
		licenseType = LicenseType.fromValue(operation);

		switch (licenseType) {
			case PRODLICENSE:
				// PROD → ipAddress 필수, expDate 들어오면 예외
				if (ipAddress == null || ipAddress.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_IPADDRESS);
				}
				if (expDate != null && !expDate.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.NOT_ALLOWED_EXPDATE_FOR_PROD);
				}
				break;

			case DEVLICENSE:
			case TEMPLICENSE:
				// DEV, TEMP → expDate 필수, ipAddress 들어오면 예외
				if (expDate == null || expDate.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_EXPDATE);
				}
				if (ipAddress != null && !ipAddress.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.NOT_ALLOWED_IP_FOR_DEV_TEMP);
				}
				break;
		}
	}
}