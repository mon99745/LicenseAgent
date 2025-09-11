# LicenseAgent

license-common (공통 모듈)

- 공통 파일 관리

license-issuer (발급 모듈)
    
- 신규 발급, 갱신, 폐기 기능 담당

license-validator (검증 모듈)

- 애플리케이션 구동 시 라이선스 유효성 검사

license-policy (정책 모듈)

- 라이선스 조건 정의 (기간, 사용자 제한, 기능 제한 등)

license-store (저장소 모듈)

- DB 연동, Redis/파일 기반 저장 가능

license-api (외부 연동 모듈)

- REST API / gRPC 제공

- 타 서비스에서 발급 및 검증 요청 가능

license-admin (관리 UI 모듈)

- 웹 콘솔 → 관리자 발급/조회/폐기 기능 제공