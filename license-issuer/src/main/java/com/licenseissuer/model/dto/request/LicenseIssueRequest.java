package com.licenseissuer.model.dto.request;

import com.licenseissuer.exception.LicenseIssuerError;
import com.licenseissuer.exception.LicenseIssuerException;
import com.licensecommon.enums.LicenseType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.licensecommon.enums.LicenseType.DEVLICENSE;
import static com.licensecommon.enums.LicenseType.PRODLICENSE;
import static com.licensecommon.enums.LicenseType.TEMPLICENSE;

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

		LicenseType type = LicenseType.fromValue(operation);
		switch (type) {
			case PRODLICENSE:
				// PROD → ipAddress 필수, expDate 들어오면 예외
				if (ipAddress == null || ipAddress.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.EMPTY_ISSUE_VALUE_IPADDRESS);
				}
				if (expDate != null && !expDate.isBlank()) {
					throw new LicenseIssuerException(LicenseIssuerError.NOT_ALLOWED_EXPDATE_FOR_PROD);
				}
				break;

			case DEVLICENSE, TEMPLICENSE:
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