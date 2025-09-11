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
	SAMPLE(LicenseIssuerError.CODE_PREFIX + "10-01", "SAMPLE-MSG", HttpStatus.BAD_REQUEST);

	public static final String CODE_PREFIX = "ISSUER-";

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	@Override
	public String toString() {
		return toCodeString();
	}
}