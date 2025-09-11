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
	EMPTY_ISSUE_VALUE(LicenseIssuerError.CODE_PREFIX + "01-04", "IP address and Expire Date is required", HttpStatus.BAD_REQUEST),

	EMPTY_UPDATE_VALUE_OPERATION(LicenseIssuerError.CODE_PREFIX + "01-05", "Operation is required", HttpStatus.BAD_REQUEST),
	EMPTY_UPDATE_VALUE_PROJECTNAME(LicenseIssuerError.CODE_PREFIX + "01-06", "Project name is required", HttpStatus.BAD_REQUEST),
	EMPTY_UPDATE_VALUE_ISSUER(LicenseIssuerError.CODE_PREFIX + "01-07", "Issuer is required", HttpStatus.BAD_REQUEST),
	EMPTY_UPDATE_VALUE(LicenseIssuerError.CODE_PREFIX + "01-08", "IP address and Expire Date is required", HttpStatus.BAD_REQUEST),

	EMPTY_READ_VALUE_ISSUER(LicenseIssuerError.CODE_PREFIX + "01-09", "Issuer is required", HttpStatus.BAD_REQUEST),


	FAIL_UPDATE_QUERY_LICENSE(LicenseIssuerError.CODE_PREFIX + "01-99", "Failed to query license for update", HttpStatus.BAD_REQUEST),




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