package com.licensevalidator.service;

import com.licensecommon.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 파일 관련 모든 검사
 */
@Slf4j
@Service
public class VerifyFileService {
	public File validate(String licPath) {
		log.info("라이센스 파일 유효성 검사: {}", licPath);
		File file = new File(licPath);
		FileUtil.validateFile(file);

		return file;
	}

	public String readToken(File file) {
		return FileUtil.readFileContent(file);
	}
}