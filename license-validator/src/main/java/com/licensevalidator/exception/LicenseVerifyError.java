package com.licensevalidator.exception;

import com.licensecommon.exception.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Validator Error
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LicenseVerifyError
		implements Error {
	EMPTY_FILE_PATH(LicenseVerifyError.CODE_PREFIX + "01-01", "filePath is empty. Please check the input value.", HttpStatus.BAD_REQUEST),
	EMPTY_VALID_VALUE_OPERATION(LicenseVerifyError.CODE_PREFIX + "01-02", "operation is null", HttpStatus.BAD_REQUEST),
	EMPTY_VALID_VALUE_IPADDRESS(LicenseVerifyError.CODE_PREFIX + "01-03", "ipAddress is null", HttpStatus.BAD_REQUEST),
	EMPTY_VALID_VALUE_EXPIRE_DATE(LicenseVerifyError.CODE_PREFIX + "01-04", "expDate is null", HttpStatus.BAD_REQUEST),
	INVALID_LICENSE(LicenseVerifyError.CODE_PREFIX + "01-05", "License is not valid.", HttpStatus.BAD_REQUEST),
	INVALID_LICENSE_EXPIRE_DATE(LicenseVerifyError.CODE_PREFIX + "01-06", "The license has expired: ", HttpStatus.BAD_REQUEST),
	INVALID_LICENSE_FILE_TYPE(LicenseVerifyError.CODE_PREFIX + "01-07", "Unsupported license type: ", HttpStatus.BAD_REQUEST),
	FAILED_LOCAL_IP_LOOKUP(LicenseVerifyError.CODE_PREFIX + "01-08", "Failed to lookup local IP address", HttpStatus.BAD_REQUEST),
	FAILED_LICENSE_DATA_EXTRACT(LicenseVerifyError.CODE_PREFIX + "01-09", "Failed to extract license data: ", HttpStatus.BAD_REQUEST),
	FAILED_LICENSE_FORGERY_VERIFY(LicenseVerifyError.CODE_PREFIX + "01-10", "License forgery verification failed: ", HttpStatus.BAD_REQUEST);

	public static final String CODE_PREFIX = "VALIDATOR-";

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	@Override
	public String toString() {
		return toCodeString();
	}
}