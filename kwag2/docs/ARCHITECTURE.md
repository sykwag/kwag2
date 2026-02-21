# KWAG2 - SaaS 멀티테넌트 프레임워크

## 📋 개요

KWAG2는 전자정부프레임워크 4.x 기반의 SaaS형 멀티테넌트 웹 애플리케이션 프레임워크입니다.  
하나의 웹 애플리케이션으로 여러 고객(테넌트)을 대상으로 서비스할 수 있으며, 각 테넌트의 요구사항을 모듈화 구조로 유연하게 적용할 수 있습니다.

## 🎯 핵심 특징

### 1. 멀티테넌트 아키텍처
- **테넌트 격리**: 각 요청에서 테넌트를 식별하고 데이터를 격리
- **동적 테넌트 감지**: Header, Cookie, URI 경로에서 테넌트 ID 자동 추출
- **ThreadLocal 기반**: 멀티스레드 환경에서 안전한 테넌트 컨텍스트 관리

### 2. 모듈화 구조
- **플러그인 아키텍처**: 기존 시스템 영향 없이 새로운 모듈 추가 가능
- **동적 로딩**: ModuleManager를 통한 런타임 모듈 관리
- **테넌트별 커스터마이징**: 각 테넌트별로 특정 모듈 활성화/비활성화 가능

### 3. 분리된 아키텍처
- **프론트엔드/백엔드 분리**: REST API 기반 완전 분리
- **향후 확장성**: JSP+jQuery에서 Vue/React 등으로 전환 가능
- **API 문서화**: Swagger를 통한 자동 문서화

### 4. 다중 데이터베이스 지원
- **MySQL 기본**: MySQL을 기본 데이터베이스로 사용
- **다중 DB 연결**: 동시에 여러 데이터베이스 연결 가능
- **DB별 SQL 분리**: 각 데이터베이스에 최적화된 SQL 작성 가능
  - `mapper/mysql/` - MySQL SQL
  - `mapper/oracle/` - Oracle SQL
  - `mapper/postgresql/` - PostgreSQL SQL

### 5. 계층화된 구조
```
Controller (API 진입점)
    ↓
Service (비즈니스 로직)
    ↓
ServiceImpl (실제 구현)
    ↓
Mapper (데이터 접근)
    ↓
VO (데이터 모델)
    ↓
Database
```

## 🏗️ 프로젝트 구조

```
kwag2/
├── src/
│   ├── main/
│   │   ├── java/kr/sy/kwag2/
│   │   │   ├── core/                    # 핵심 프레임워크
│   │   │   │   ├── context/             # MultiTenantContext
│   │   │   │   ├── interceptor/         # 필터 및 인터셉터
│   │   │   │   ├── controller/          # BaseController
│   │   │   │   ├── service/             # 기본 서비스
│   │   │   │   ├── mapper/              # 기본 매퍼
│   │   │   │   └── config/              # Spring 설정
│   │   │   ├── module/                  # 모듈 시스템
│   │   │   │   ├── core/                # IModule, AbstractModule
│   │   │   │   ├── custom/              # 커스텀 모듈
│   │   │   │   └── manager/             # ModuleManager
│   │   │   ├── db/                      # 다중 DB 지원
│   │   │   ├── api/                     # REST API
│   │   │   └── common/                  # 공통 유틸
│   │   ├── resources/
│   │   │   ├── application*.properties  # 설정 파일
│   │   │   ├── spring/                  # Spring XML 설정
│   │   │   └── mapper/                  # MyBatis 매퍼
│   │   └── webapp/
│   │       ├── WEB-INF/web.xml
│   │       ├── resources/               # 정적 리소스
│   │       └── views/                   # JSP 뷰
│   └── test/                            # 테스트 코드
├── docs/                                # 문서
├── scripts/                             # 배포 스크립트
└── pom.xml                              # Maven 설정
```

## 📡 API 엔드포인트

### 사용자 관리 API
- `GET /api/v1/users` - 모든 사용자 조회
- `GET /api/v1/users/{userId}` - 특정 사용자 조회
- `GET /api/v1/users/search/username?username=xxx` - 사용자명으로 조회
- `GET /api/v1/users/search/email?email=xxx` - 이메일로 조회
- `POST /api/v1/users` - 새 사용자 생성
- `PUT /api/v1/users/{userId}` - 사용자 정보 수정
- `DELETE /api/v1/users/{userId}` - 사용자 삭제

### Swagger API 문서
- `http://localhost:8080/kwag2/swagger-ui.html` - 개발 환경

## 🔧 개발 환경 설정

### 필수 환경
- Java 8 이상
- Maven 3.6.0 이상
- MySQL 5.7 이상
- Tomcat 8.5 이상

### 로컬 개발 셋업

1. **프로젝트 클론**
   ```bash
   git clone <repository-url>
   cd kwag2
   ```

2. **데이터베이스 생성**
   ```sql
   CREATE DATABASE kwag2_dev CHARACTER SET utf8mb4;
   CREATE DATABASE kwag2_dev_secondary CHARACTER SET utf8mb4;
   ```

3. **테이블 생성** (SQL 스크립트 실행)
   ```bash
   mysql -u root -p kwag2_dev < scripts/schema.sql
   ```

4. **개발 환경 빌드**
   ```bash
   mvn clean package -P dev
   ```

5. **Tomcat에 배포**
   ```bash
   cp target/kwag2.war $TOMCAT_HOME/webapps/
   cd $TOMCAT_HOME/bin && ./startup.sh
   ```

6. **애플리케이션 접근**
   - 메인 페이지: `http://localhost:8080/kwag2/`
   - Swagger UI: `http://localhost:8080/kwag2/swagger-ui.html`
   - API 기본 URL: `http://localhost:8080/kwag2/api/v1/`

## 🚀 배포 및 빌드

### 개발 환경 배포
```bash
chmod +x scripts/deploy-dev.sh
./scripts/deploy-dev.sh
```

### 운영 환경 배포
```bash
chmod +x scripts/deploy-prod.sh
./scripts/deploy-prod.sh
```

### 빌드 프로파일
- **dev**: 개발 환경 (디버그 활성화, 상세 로깅)
- **prod**: 운영 환경 (최소 로깅, 최적화)

## 🔐 보안 설정

### 멀티테넌트 인증
- 각 요청에서 테넌트 ID를 추출하여 컨텍스트에 저장
- 데이터베이스 쿼리 시 테넌트 ID 필터링 자동 적용
- 테넌트 간 데이터 격리 보장

### 기본 보안 설정
- XSS 방지
- CSRF 토큰
- 세션 관리

## 📚 모듈 개발 가이드

[MODULES_GUIDE.md](./MODULES_GUIDE.md) 참조

## 🔌 다중 데이터베이스 설정

[DATABASE_SETUP.md](./DATABASE_SETUP.md) 참조

## 📝 API 문서

[API_DOCUMENTATION.md](./API_DOCUMENTATION.md) 참조

## 🐛 트러블슈팅

### 문제: 테넌트 ID가 인식되지 않음
**해결**: MultiTenantContextFilter가 web.xml에 등록되어 있는지 확인

### 문제: 모듈이 로드되지 않음
**해결**: 모듈이 ModuleManager에 등록되었는지 확인

### 문제: 데이터베이스 연결 실패
**해결**: application-dev.properties의 JDBC 설정 확인

## 📞 지원

문제가 있거나 기능 요청사항이 있으면 프로젝트 이슈 트래커를 사용해주세요.

## 📄 라이선스

[LICENSE](./LICENSE) 파일 참조