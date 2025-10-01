package com.licenseissuer.exception;

import com.licensecommon.exception.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Issuer Error
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LicenseIssuerError
		implements Error {

	EMPTY_ISSUE_VALUE_OPERATION(LicenseIssuerError.CODE_PREFIX + "01-01", "Operation is required", HttpStatus.BAD_REQUEST),
	EMPTY_ISSUE_VALUE_PROJECTNAME(LicenseIssuerError.CODE_PREFIX + "01-02", "Project name is required", HttpStatus.BAD_REQUEST),
	EMPTY_ISSUE_VALUE_ISSUER(LicenseIssuerError.CODE_PREFIX + "01-03", "Issuer is required", HttpStatus.BAD_REQUEST),

	EMPTY_UPDATE_VALUE_LICENSEID(LicenseIssuerError.CODE_PREFIX + "01-04", "License-ID is required", HttpStatus.BAD_REQUEST),


	EMPTY_UPDATE_VALUE_OPERATION(LicenseIssuerError.CODE_PREFIX + "01-05", "Operation is required", HttpStatus.BAD_REQUEST),
	EMPTY_UPDATE_VALUE_PROJECTNAME(LicenseIssuerError.CODE_PREFIX + "01-06", "Project name is required", HttpStatus.BAD_REQUEST),
	EMPTY_UPDATE_VALUE_ISSUER(LicenseIssuerError.CODE_PREFIX + "01-07", "Issuer is required", HttpStatus.BAD_REQUEST),

	EMPTY_READ_VALUE_ISSUER(LicenseIssuerError.CODE_PREFIX + "01-09", "Issuer is required", HttpStatus.BAD_REQUEST),

	INVALID_OPERATION(LicenseIssuerError.CODE_PREFIX + "01-10", "Unsupported license type", HttpStatus.BAD_REQUEST),

	EMPTY_ISSUE_VALUE_IPADDRESS(LicenseIssuerError.CODE_PREFIX + "01-11", "Production license requires an IP address.", HttpStatus.BAD_REQUEST),
	NOT_ALLOWED_EXPDATE_FOR_PROD(LicenseIssuerError.CODE_PREFIX + "01-12", "Production license does not allow an expiration date.", HttpStatus.BAD_REQUEST),
	EMPTY_ISSUE_VALUE_EXPDATE(LicenseIssuerError.CODE_PREFIX + "01-13", "Development or temporary license requires an expiration date.", HttpStatus.BAD_REQUEST),
	NOT_ALLOWED_IP_FOR_DEV_TEMP(LicenseIssuerError.CODE_PREFIX + "01-14", "Development or temporary license must not contain an IP address.", HttpStatus.BAD_REQUEST),

	EMPTY_READ_VALUE_LICENSEID(LicenseIssuerError.CODE_PREFIX + "01-15", "Production license requires an IP address to be allowed", HttpStatus.BAD_REQUEST),
	INVALID_ISSUE_VALUE_IPADDRESS_FORMAT(LicenseIssuerError.CODE_PREFIX + "01-16", "Invalid IP address format.", HttpStatus.BAD_REQUEST),
	EMPTY_ISSUE_VALUE_ISSUER_IP(LicenseIssuerError.CODE_PREFIX + "01-17", "Issuer IP is required", HttpStatus.BAD_REQUEST),
	INVALID_ISSUE_VALUE_ISSUER_IP_FORMAT(LicenseIssuerError.CODE_PREFIX + "01-17", "Invalid issuer IP format.", HttpStatus.BAD_REQUEST),



	LICENSE_NOT_FOUND(LicenseIssuerError.CODE_PREFIX + "01-99", "License not found", HttpStatus.BAD_REQUEST),





	FAIL_CREATE_DIRECTORY(LicenseIssuerError.CODE_PREFIX + "99-03", "Failed to create directory", HttpStatus.INTERNAL_SERVER_ERROR),
	FAIL_SAVE_LICENSE(LicenseIssuerError.CODE_PREFIX + "99-02", "Failed to save license file", HttpStatus.INTERNAL_SERVER_ERROR),
	EMPTY_RESOURCE(LicenseIssuerError.CODE_PREFIX + "99-01", "Resource is null", HttpStatus.INTERNAL_SERVER_ERROR);
	public static final String CODE_PREFIX = "ISSUER-";

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	@Override
	public String toString() {
		return toCodeString();
	}
}