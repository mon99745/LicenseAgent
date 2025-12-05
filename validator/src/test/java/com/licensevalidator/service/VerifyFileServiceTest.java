package com.licensevalidator.service;

import com.licensecommon.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockedStatic;

import java.io.File;
import static org.mockito.Mockito.*;

@DisplayName("파일 관련 모든 검사 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class VerifyFileServiceTest {
	private VerifyFileService verifyFileService = new VerifyFileService();

	@Order(1)
	@DisplayName("01. 라이센스 파일 유효성 검사 성공 케이스")
	@Test
	void validate_success() {
		String path = "license.lic";
		File fakeFile = new File(path);

		try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

			mocked.when(() -> FileUtil.validateFile(any(File.class)))
					.thenAnswer(invocation -> null);

			File result = verifyFileService.validate(path);

			assertNotNull(result);
			assertEquals(fakeFile.getPath(), result.getPath());
		}
	}

	@Order(2)
	@DisplayName("02. 라이센스 파일 토큰 읽기 성공 케이스")
	@Test
	void readToken_success() {
		File fakeFile = new File("license.lic");

		try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

			mocked.when(() -> FileUtil.readFileContent(fakeFile))
					.thenReturn("FAKE_TOKEN");

			String token = verifyFileService.readToken(fakeFile);

			assertEquals("FAKE_TOKEN", token);
		}
	}

	@Order(3)
	@DisplayName("03. 라이센스 파일 유효성 검사 실패 케이스 - 파일 없음")
	@Test
	void validate_fail_whenFileInvalid() {
		String path = "invalid.lic";

		try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

			mocked.when(() -> FileUtil.validateFile(any(File.class)))
					.thenThrow(new RuntimeException("FILE_NOT_FOUND"));

			RuntimeException ex = assertThrows(
					RuntimeException.class,
					() -> verifyFileService.validate(path)
			);

			assertEquals("FILE_NOT_FOUND", ex.getMessage());
		}
	}

	@Order(4)
	@DisplayName("04. 라이센스 파일 토큰 읽기 실패 케이스 - 읽기 오류")
	@Test
	void readToken_fail_whenReadError() {
		File fakeFile = new File("license.lic");

		try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

			mocked.when(() -> FileUtil.readFileContent(fakeFile))
					.thenThrow(new RuntimeException("READ_ERROR"));

			RuntimeException ex = assertThrows(
					RuntimeException.class,
					() -> verifyFileService.readToken(fakeFile)
			);

			assertEquals("READ_ERROR", ex.getMessage());
		}
	}
}