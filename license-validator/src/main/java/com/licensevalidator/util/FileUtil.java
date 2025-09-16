package com.licensevalidator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 관련 공통 유틸리티 클래스
 * - 파일 유효성 검증
 * - 파일 내용 읽기 (전체 문자열 또는 라인 단위)
 */
public class FileUtil {
	private FileUtil() {}

	/**
	 * 파일 유효성 검증
	 * - 파일 존재 여부, 읽기 가능 여부
	 * - 파일 크기 제한 (10MB 예시)
	 * - 확장자 제한 (.lic)
	 * - 내용 확인: 문자 존재 여부
	 *
	 * @param file 검증할 파일
	 * @throws IllegalArgumentException 검증 실패 시 예외 발생
	 */
	public static void validateFile(File file) {
		// 1. null 체크
		if (file == null) {
			throw new IllegalArgumentException("파일이 null 입니다.");
		}

		// 2. 파일 존재 여부
		if (!file.exists()) {
			throw new IllegalArgumentException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
		}

		// 3. 파일인지 확인
		if (!file.isFile()) {
			throw new IllegalArgumentException("파일이 아닌 경로입니다: " + file.getAbsolutePath());
		}

		// 4. 읽기 권한 확인
		if (!file.canRead()) {
			throw new IllegalArgumentException("파일을 읽을 수 없습니다: " + file.getAbsolutePath());
		}

		// 5. 크기 제한 (10MB)
		long maxSize = 10 * 1024 * 1024;
		if (file.length() > maxSize) {
			throw new IllegalArgumentException("파일 크기가 10MB를 초과했습니다.");
		}

		// 6. 확장자 제한
		String fileName = file.getName().toLowerCase();
		if (!(fileName.endsWith(".lic"))) {
			throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + fileName);
		}

		// 7. 파일 내용에 문자가 존재하는지 확인
		boolean hasText = false;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

			int ch;
			while ((ch = reader.read()) != -1) {
				// 공백, 개행 등은 무시하고 실제 문자가 있는지 확인
				if (!Character.isWhitespace(ch)) {
					hasText = true;
					break; // 문자 발견 시 즉시 종료
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("파일을 읽는 중 오류가 발생했습니다 (깨졌을 수 있음): " + file.getAbsolutePath(), e);
		}

		// 문자가 없으면 예외 발생
		if (!hasText) {
			throw new IllegalArgumentException("파일에 문자가 존재하지 않습니다 (빈 파일 또는 공백만 있음).");
		}
	}

	/**
	 * 파일 전체 내용을 문자열로 읽어오기
	 *
	 * @param file 읽을 파일
	 * @return 파일 전체 내용
	 * @throws IllegalArgumentException 파일 읽기 실패 시 발생
	 */
	public static String readFileContent(File file) {
		StringBuilder content = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append(System.lineSeparator());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("파일을 읽는 중 오류가 발생했습니다: " + file.getAbsolutePath(), e);
		}

		return content.toString();
	}

	/**
	 * 파일 내용을 라인 단위로 읽어오기
	 *
	 * @param file 읽을 파일
	 * @return 파일 내용 라인 리스트
	 * @throws IllegalArgumentException 파일 읽기 실패 시 발생
	 */
	public static List<String> readFileLines(File file) {
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("파일을 읽는 중 오류가 발생했습니다: " + file.getAbsolutePath(), e);
		}

		return lines;
	}
}
