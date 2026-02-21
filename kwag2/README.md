# KWAG2 - SaaS 멀티테넌트 프레임워크

> 전자정부프레임워크 4.x 기반의 **SaaS형 멀티테넌트 웹 애플리케이션 프레임워크**

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6.0+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](#license)

## 🎯 개요

KWAG2는 하나의 웹 애플리케이션으로 여러 고객(테넌트)을 대상으로 SaaS 서비스를 제공할 수 있도록 설계된 프레임워크입니다.

### 핵심 특징

✅ **멀티테넌트 지원** - 완벽한 테넌트 격리 및 동적 감지  
✅ **모듈화 구조** - 플러그인 기반 모듈 시스템으로 쉬운 확장  
✅ **분리된 아키텍처** - REST API 기반 Frontend/Backend 완전 분리  
✅ **다중 데이터베이스** - MySQL, Oracle, PostgreSQL 등 동시 지원  
✅ **API 문서화** - Swagger를 통한 자동 API 문서화  
✅ **Dev/Prod 분리** - Maven 프로파일을 통한 환경별 배포  

## 🚀 빠른 시작

### 1단계: 준비

```bash
# 저장소 클론
git clone <repository-url>
cd kwag2

# 데이터베이스 생성
mysql -u root -p -e "CREATE DATABASE kwag2_dev CHARACTER SET utf8mb4"
```

### 2단계: 개발 환경 빌드

```bash
# 개발 환경 빌드
mvn clean package -P dev

# 또는 배포 스크립트 사용
chmod +x scripts/deploy-dev.sh
./scripts/deploy-dev.sh
```

### 3단계: 애플리케이션 실행

```bash
# Tomcat에 배포된 WAR 실행
# http://localhost:8080/kwag2/

# Swagger UI (API 문서)
# http://localhost:8080/kwag2/swagger-ui.html
```

## 📁 프로젝트 구조

```
kwag2/
├── src/
│   ├── main/
│   │   ├── java/kr/sy/kwag2/
│   │   │   ├── core/              # 핵심 프레임워크
│   │   │   ├── module/            # 모듈 시스템
│   │   │   ├── db/                # 다중 DB 관리
│   │   │   ├── api/               # REST API
│   │   │   └── common/            # 공통 유틸
│   │   ├── resources/             # 설정 및 리소스
│   │   └── webapp/                # JSP 뷰 및 정적 파일
│   └── test/                      # 테스트 코드
├── docs/                          # 프로젝트 문서
├── scripts/                       # 배포 스크립트
└── pom.xml                        # Maven 설정
```

## 📚 문서

| 문서 | 설명 |
|------|------|
| [ARCHITECTURE.md](./docs/ARCHITECTURE.md) | 시스템 아키텍처 및 전체 구조 |
| [MODULES_GUIDE.md](./docs/MODULES_GUIDE.md) | 모듈 개발 가이드 |
| [DATABASE_SETUP.md](./docs/DATABASE_SETUP.md) | 다중 데이터베이스 설정 |

## 🔧 기본 사용 예

### API 호출

```bash
# 사용자 조회
curl -H "X-Tenant-ID: tenant-001" \
     http://localhost:8080/kwag2/api/v1/users

# 사용자 생성
curl -X POST \
     -H "X-Tenant-ID: tenant-001" \
     -H "Content-Type: application/json" \
     -d '{"username":"user1","email":"user@example.com"}' \
     http://localhost:8080/kwag2/api/v1/users
```

### 모듈 개발

```java
@Component
public class MyModule extends AbstractModule {
    
    public MyModule() {
        super("my-module", "내 모듈", "1.0.0", "설명");
    }
    
    @Override
    public void activate() {
        super.activate();
        // 활성화 로직
    }
}
```

## 🔐 멀티테넌트 식별

테넌트는 다음 방식으로 식별됩니다 (우선순위 순):

1. **HTTP Header**: `X-Tenant-ID`
2. **Cookie**: `tenantId`
3. **URI Path**: `/tenant/{tenantId}/...`
4. **기본값**: `default`

## 🔄 빌드 및 배포

### 개발 환경
```bash
mvn clean package -P dev
./scripts/deploy-dev.sh
```

### 운영 환경
```bash
mvn clean package -P prod
./scripts/deploy-prod.sh
```

## 📊 시스템 요구사항

- **Java**: 8 이상
- **Maven**: 3.6.0 이상
- **Tomcat**: 8.5 이상
- **MySQL**: 5.7 이상 (또는 Oracle, PostgreSQL)

## 🛠️ 기술 스택

| 계층 | 기술 |
|------|------|
| **Frontend** | JSP, jQuery (향후 Vue/React 지원 가능) |
| **Framework** | 전자정부프레임워크 4.x, Spring 5.3 |
| **ORM** | MyBatis 3.5 |
| **DB Pool** | HikariCP |
| **API Docs** | Springfox Swagger 3.0 |
| **Logging** | SLF4J, Log4j2 |

## 📈 성능 최적화

- **HikariCP**: 고성능 커넥션 풀
- **MyBatis 캐싱**: SQL 실행 결과 캐싱
- **ThreadLocal**: 멀티테넌트 컨텍스트 효율적 관리
- **비동기 처리**: 필요시 추가 가능

## 🐛 알려진 문제 및 해결책

| 문제 | 해결책 |
|------|--------|
| 테넌트 ID 인식 안 됨 | MultiTenantContextFilter 확인 |
| 모듈 로드 실패 | ModuleManager 등록 확인 |
| DB 연결 오류 | application.properties JDBC 설정 확인 |

## 🤝 기여

프로젝트에 기여하려면:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다. 자세한 내용은 [LICENSE](./LICENSE) 파일을 참조하세요.

## 📞 지원

- **문제 보고**: [Issue Tracker](../../issues)
- **질문**: [Discussions](../../discussions)
- **이메일**: dev@example.com

## 🙏 감사의 말

- 전자정부프레임워크 팀
- Spring Framework 팀
- MyBatis 팀

---

**Made with ❤️ for building scalable SaaS applications**