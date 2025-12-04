# LicenseAgent
## Introduction
### license-common (공통 모듈)
- 공통 파일 관리(Common File Management)
    - Common API Document
    - Common Exception
    - Common Properties
    - Common Utils 

### license-issuer (발급 모듈)
- 라이센스 발급 관리(License Issue Management)
    - 신규 발급
    - 이력 조회
    - 정보 변경 

### license-web (사용자 UI 모듈)
- 사용자 웹 콘솔(Client Web Console)
    - 라이센스 발급 (요청 → 발급 모듈)
    - 라이센스 이력 조회 
    - 라이센스 상태 변경 (요청 → 발급 모듈)

### license-webadmin (관리 UI 모듈)
- 관리자 웹 콘솔(Admin Web Console)
    - 라이센스 발급 (요청 → 발급 모듈)
    - 라이센스 이력 조회 
    - 라이센스 상태 변경 (요청 → 발급 모듈)
    - 라이센스 발급 현황 통계 조회

### validator (검증 라이브러리, 라이브러리 제공용)
- 라이센스 검증 (License Verification Management)
    - 검증
        - 위변조 검증
        - 소유자 검증
        - 유효성(할당 IP/만료일자) 검증
          
### validator-guide (라이센스 사용 가이드, 프로젝트 제공)
- 라이센스 사용 가이드, 샘플 프로젝트(License Guide, Sample Project)
    - 목적 : 어플리케이션 구동 시 라이센스 사용 가이드 - **제공용**
        - Import Library(**/lib/*)
        - Defining Dependencies(build.gradle)
        - Defining Initialization(*.class)

[//]: # (license-policy &#40;정책 모듈&#41;)

[//]: # (- 라이선스 조건 정의 &#40;기간, 사용자 제한, 기능 제한 등&#41;)

[//]: # ()
[//]: # (license-store &#40;저장소 모듈&#41;)

[//]: # (- DB 연동, Redis/파일 기반 저장 가능)

[//]: # ()
[//]: # (license-api &#40;외부 연동 모듈&#41;)

[//]: # (- REST API / gRPC 제공)

[//]: # (- 타 서비스에서 발급 및 검증 요청 가능)


## Reference
1. https://github.com/mon99745/TokenReference
2. https://github.com/mon99745/CommonReference


