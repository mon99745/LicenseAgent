package com.licensecommon.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date parse(String dateStr) {
		return parse(dateStr, DEFAULT_FORMAT);
	}

	/**
	 * 문자열을 Date로 변환 (사용자 지정 포맷)
	 */
	public static Date parse(String dateStr, String format) {
		if (dateStr == null || dateStr.isBlank()) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("날짜 변환 실패: " + dateStr + ", 포맷: " + format, e);
		}
	}

	/**
	 * Date를 문자열로 변환 (기본 포맷)
	 */
	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}

	/**
	 * Date를 문자열로 변환 (사용자 지정 포맷)
	 */
	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}