package com.licensevalidator.exception;

import com.licensecommon.exception.DefaultException;
import com.licensecommon.exception.Error;

/**
 * Validator Exception
 */
public class LicenseVerifyException
		extends DefaultException {
	public LicenseVerifyException(String message) {
		this(message, null);
	}

	public LicenseVerifyException(Throwable cause) {
		this((String) null, cause);
	}

	public LicenseVerifyException(String message, Throwable cause) {
		this(Error.DefaultError.NONE, message, cause);
	}

	public LicenseVerifyException(Error error) {
		this(error, (String) null);
	}

	public LicenseVerifyException(Error error, String message) {
		this(error, message, null);
	}

	public LicenseVerifyException(Error error, Throwable cause) {
		this(error, null, cause);
	}

	/**
	 * Validator 예외 생성자
	 *
	 * @param error   에러
	 * @param message 메세지
	 * @param cause   원인 예외
	 */
	public LicenseVerifyException(Error error, String message, Throwable cause) {
		super(error, message, cause);
	}
}