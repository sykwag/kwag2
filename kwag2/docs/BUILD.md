# 빌드 및 배포 가이드

## 📦 사전 요구사항

### 필수 소프트웨어
- **Java**: JDK 8 이상
- **Maven**: 3.6.0 이상
- **Tomcat**: 8.5 이상
- **MySQL**: 5.7 이상 (또는 선택한 데이터베이스)

### 설치 확인

```bash
# Java 확인
java -version

# Maven 확인
mvn --version

# Tomcat 확인 (TOMCAT_HOME 환경변수 설정 필요)
echo $TOMCAT_HOME
```

## 🔨 빌드 프로세스

### 1. 개발 환경 빌드

```bash
# 프로젝트 디렉토리 이동
cd kwag2

# 개발 환경 빌드 (dev 프로파일 사용)
mvn clean package -P dev
```

**결과:**
```
target/kwag2.war
```

### 2. 운영 환경 빌드

```bash
# 운영 환경 빌드 (prod 프로파일 사용)
mvn clean package -P prod
```

**결과:**
```
target/kwag2.war
```

### 3. 테스트 포함 빌드

```bash
# 모든 테스트 포함
mvn clean package -P dev

# 테스트 제외
mvn clean package -P dev -DskipTests
```

## 🚀 배포

### 방법 1: 스크립트를 이용한 배포

#### 개발 환경
```bash
chmod +x scripts/deploy-dev.sh
./scripts/deploy-dev.sh
```

#### 운영 환경
```bash
chmod +x scripts/deploy-prod.sh
./scripts/deploy-prod.sh
```

### 방법 2: 수동 배포

#### Step 1: WAR 파일 준비
```bash
mvn clean package -P dev -DskipTests
```

#### Step 2: Tomcat 중지 (필요시)
```bash
$TOMCAT_HOME/bin/shutdown.sh
```

#### Step 3: 기존 배포 제거
```bash
rm -rf $TOMCAT_HOME/webapps/kwag2
rm -f $TOMCAT_HOME/webapps/kwag2.war
```

#### Step 4: WAR 파일 복사
```bash
cp target/kwag2.war $TOMCAT_HOME/webapps/
```

#### Step 5: Tomcat 시작
```bash
$TOMCAT_HOME/bin/startup.sh
```

#### Step 6: 배포 확인
```bash
# 로그 확인
tail -f $TOMCAT_HOME/logs/catalina.out

# 애플리케이션 접근
curl http://localhost:8080/kwag2/
```

## 🔧 빌드 프로파일

### Dev 프로파일 (`-P dev`)

**특징:**
- 디버그 모드 활성화
- 상세한 로깅 (DEBUG 레벨)
- Swagger UI 활성화
- 로컬 데이터베이스 사용

**설정 파일:**
- `application-dev.properties`

**사용:**
```bash
mvn clean package -P dev
```

### Prod 프로파일 (`-P prod`)

**특징:**
- 디버그 모드 비활성화
- 최소 로깅 (INFO/WARN 레벨)
- Swagger UI 비활성화
- 운영 데이터베이스 사용

**설정 파일:**
- `application-prod.properties`

**사용:**
```bash
mvn clean package -P prod
```

## 📋 체크리스트

배포 전 다음을 확인하세요:

- [ ] Java 8 이상 설치됨
- [ ] Maven 3.6.0 이상 설치됨
- [ ] TOMCAT_HOME 환경변수 설정됨
- [ ] 데이터베이스가 실행 중
- [ ] application-{env}.properties 설정 확인
- [ ] pom.xml의 의존성 다운로드 완료
- [ ] 방화벽에서 포트 8080 개방됨

## 🐛 트러블슈팅

### 문제 1: Maven 빌드 실패

**증상:**
```
[ERROR] BUILD FAILURE
```

**해결책:**
```bash
# Maven 캐시 삭제 후 재시도
mvn clean -U package -P dev

# 또는 로컬 저장소 초기화
rm -rf ~/.m2/repository
mvn clean package -P dev
```

### 문제 2: Tomcat 배포 오류

**증상:**
```
Application at context path /kwag2 failed to start
```

**해결책:**
1. `$TOMCAT_HOME/logs/catalina.out` 확인
2. 데이터베이스 연결 확인
3. 포트 충돌 확인 (`netstat -tulpn | grep 8080`)

### 문제 3: 데이터베이스 연결 실패

**증상:**
```
SQLException: Cannot get a connection
```

**해결책:**
```bash
# 1. 데이터베이스 실행 확인
mysql -u root -p

# 2. application.properties 설정 확인
cat src/main/resources/application-dev.properties | grep jdbc

# 3. 데이터베이스 생성
mysql -u root -p -e "CREATE DATABASE kwag2_dev CHARACTER SET utf8mb4"
```

### 문제 4: 메모리 부족

**증상:**
```
OutOfMemoryError: Java heap space
```

**해결책:**
```bash
# CATALINA_OPTS 설정
export CATALINA_OPTS="-Xms512m -Xmx1024m"
$TOMCAT_HOME/bin/startup.sh
```

## 📊 빌드 성능 최적화

### Maven 병렬 빌드

```bash
# 멀티스레드 빌드 (시스템 코어 수 만큼)
mvn clean package -T 1C -P dev
```

### 의존성 다운로드 병렬화

`~/.m2/settings.xml`:
```xml
<settings>
  <parallelBuild>true</parallelBuild>
</settings>
```

## 📈 운영 환경 배포 팁

### 1. Blue-Green 배포

```bash
# Blue (현재) 환경 유지
# Green (새로운) 환경에 배포
# 테스트 완료 후 전환
```

### 2. 백업 전략

```bash
# 배포 전 백업
cp $TOMCAT_HOME/webapps/kwag2.war ./backups/kwag2_$(date +%Y%m%d_%H%M%S).war
```

### 3. 롤백 계획

```bash
# 배포 실패 시 이전 버전 복원
cp ./backups/kwag2_backup.war $TOMCAT_HOME/webapps/kwag2.war
```

## 📚 추가 리소스

- [Maven 공식 문서](https://maven.apache.org/guides/)
- [Tomcat 배포 가이드](https://tomcat.apache.org/tomcat-8.5-doc/deployer-howto.html)
- [Spring Boot 배포](https://spring.io/guides/gs/spring-boot-docker/)

## 🎯 다음 단계

배포 후:
1. http://localhost:8080/kwag2/ 접속 확인
2. http://localhost:8080/kwag2/swagger-ui.html 에서 API 테스트
3. 로그 파일 확인: `logs/kwag2.log`
4. 데이터베이스 연결 확인