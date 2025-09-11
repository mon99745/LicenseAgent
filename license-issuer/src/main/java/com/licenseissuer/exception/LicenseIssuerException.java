package com.licenseissuer.exception;

import com.licensecommon.exception.DefaultException;
import com.licensecommon.exception.Error;

/**
 * Issuer Exception
 */
public class LicenseIssuerException
		extends DefaultException {
	public LicenseIssuerException(String message) {
		this(message, null);
	}

	public LicenseIssuerException(Throwable cause) {
		this((String) null, cause);
	}

	public LicenseIssuerException(String message, Throwable cause) {
		this(Error.DefaultError.NONE, message, cause);
	}

	public LicenseIssuerException(Error error) {
		this(error, (String) null);
	}

	public LicenseIssuerException(Error error, String message) {
		this(error, message, null);
	}

	public LicenseIssuerException(Error error, Throwable cause) {
		this(error, null, cause);
	}

	/**
	 * Issuer 예외 생성자
	 *
	 * @param error   에러
	 * @param message 메세지
	 * @param cause   원인 예외
	 */
	public LicenseIssuerException(Error error, String message, Throwable cause) {
		super(error, message, cause);
	}
}
