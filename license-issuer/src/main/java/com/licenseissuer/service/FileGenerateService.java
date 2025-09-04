package com.licenseissuer.service;

import com.licenseissuer.model.LicenseType;
import org.springframework.stereotype.Service;

@Service
public class FileGenerateService {
	/**
	 * 파일 생성
	 */
	public void createFile(String str, LicenseType type) {
		// 01. 문자열을 담는 파일 생성 (라이센스명_계약명_발급일_타입.lic)
	}
}