package com.licenseissuer.config;

import com.security.jsonwebtoken.config.RsaKeyGenerator;
import com.security.jsonwebtoken.config.VerifyProperties;
import com.security.jsonwebtoken.service.TokenSerivce;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 추후 업데이트 예정
 * 라이브러리 스웨거 설정과 현 패키지 설정의 충돌
 */
@Configuration
@Import({TokenSerivce.class, RsaKeyGenerator.class, VerifyProperties.class})
public class ExternalConfig {
}