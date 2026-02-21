# 다중 데이터베이스 설정 가이드

## 📚 소개

KWAG2는 여러 개의 데이터베이스를 동시에 사용할 수 있습니다. MySQL, Oracle, PostgreSQL 등 다양한 데이터베이스를 지원하며, 각 데이터베이스에 최적화된 SQL을 작성할 수 있습니다.

## 🎯 지원하는 데이터베이스

- **MySQL** 5.7 이상 (기본)
- **Oracle** 11g 이상
- **PostgreSQL** 9.5 이상
- **기타**: 표준 JDBC 드라이버 지원하는 모든 DB

## 📝 기본 설정

### application-dev.properties

```properties
# 주 데이터베이스 (기본)
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/kwag2_dev?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
jdbc.username=root
jdbc.password=root
jdbc.pool.max=10
jdbc.pool.min=3

# 보조 데이터베이스 (다중 DB 지원)
jdbc.secondary.driver=com.mysql.cj.jdbc.Driver
jdbc.secondary.url=jdbc:mysql://localhost:3306/kwag2_dev_secondary?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
jdbc.secondary.username=root
jdbc.secondary.password=root
```

### application-prod.properties

```properties
# 운영 환경 설정
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://prod-db-server:3306/kwag2?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=true
jdbc.username=kwag2_user
jdbc.password=SECURE_PASSWORD
jdbc.pool.max=30
jdbc.pool.min=10

jdbc.secondary.driver=com.mysql.cj.jdbc.Driver
jdbc.secondary.url=jdbc:mysql://prod-db-server2:3306/kwag2_secondary?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=true
jdbc.secondary.username=kwag2_user
jdbc.secondary.password=SECURE_PASSWORD
```

## 🔧 다중 DB 커넥션 설정

### Spring Bean 설정 (spring-db.xml)

```xml
<!-- 주 DataSource -->
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
  <constructor-arg>
    <bean class="com.zaxxer.hikari.HikariConfig">
      <property name="jdbcUrl" value="${jdbc.url}" />
      <property name="username" value="${jdbc.username}" />
      <property name="password" value="${jdbc.password}" />
      <property name="driverClassName" value="${jdbc.driver}" />
      <property name="maximumPoolSize" value="${jdbc.pool.max}" />
    </bean>
  </constructor-arg>
</bean>

<!-- 보조 DataSource -->
<bean id="dataSourceSecondary" class="com.zaxxer.hikari.HikariDataSource">
  <!-- 유사하게 설정 -->
</bean>

<!-- SqlSessionFactory (주) -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource" />
  <property name="mapperLocations" value="classpath:mapper/mysql/**/*.xml" />
</bean>

<!-- SqlSessionFactory (보조) -->
<bean id="sqlSessionFactorySecondary" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSourceSecondary" />
  <property name="mapperLocations" value="classpath:mapper/mysql/**/*.xml" />
</bean>
```

## 💾 데이터베이스 선택

### MultiDBManager 사용

```java
@Autowired
private MultiDBManager multiDBManager;

public void doSomething() {
    // 주 데이터베이스 사용
    SqlSessionTemplate defaultTemplate = multiDBManager.getDefaultSqlSessionTemplate();
    
    // 보조 데이터베이스 사용
    SqlSessionTemplate secondaryTemplate = multiDBManager.getSqlSessionTemplate("secondary");
}
```

### Service에서의 사용

```java
@Service
public class DataService {
    
    @Autowired
    private MultiDBManager multiDBManager;
    
    public void syncData() {
        // 주 DB에서 데이터 읽기
        SqlSessionTemplate defaultTemplate = multiDBManager.getDefaultSqlSessionTemplate();
        List<Data> data = defaultTemplate.selectList("selectAllData");
        
        // 보조 DB에 데이터 쓰기
        SqlSessionTemplate secondaryTemplate = multiDBManager.getSqlSessionTemplate("secondary");
        data.forEach(d -> secondaryTemplate.insert("insertData", d));
    }
}
```

## 📊 데이터베이스별 SQL 작성

### 디렉토리 구조

```
src/main/resources/mapper/
├── mysql/
│   ├── UserMapper.xml
│   ├── InvoiceMapper.xml
│   └── ...
├── oracle/
│   ├── UserMapper.xml
│   ├── InvoiceMapper.xml
│   └── ...
└── postgresql/
    ├── UserMapper.xml
    ├── InvoiceMapper.xml
    └── ...
```

### MySQL용 SQL (mapper/mysql/UserMapper.xml)

```xml
<mapper namespace="kr.sy.kwag2.core.mapper.UserMapper">
  <select id="selectById" resultType="UserVO">
    SELECT user_id, username, email
    FROM user
    WHERE user_id = #{id}
  </select>
  
  <insert id="insert" useGeneratedKeys="true" keyProperty="userId">
    INSERT INTO user (username, email) 
    VALUES (#{username}, #{email})
  </insert>
</mapper>
```

### Oracle용 SQL (mapper/oracle/UserMapper.xml)

```xml
<mapper namespace="kr.sy.kwag2.core.mapper.UserMapper">
  <select id="selectById" resultType="UserVO">
    SELECT USER_ID, USERNAME, EMAIL
    FROM USER_TBL
    WHERE USER_ID = #{id}
  </select>
  
  <insert id="insert">
    <selectKey keyProperty="userId" resultType="String" order="BEFORE">
      SELECT USER_SEQ.NEXTVAL FROM DUAL
    </selectKey>
    INSERT INTO USER_TBL (USER_ID, USERNAME, EMAIL) 
    VALUES (#{userId}, #{username}, #{email})
  </insert>
</mapper>
```

### PostgreSQL용 SQL (mapper/postgresql/UserMapper.xml)

```xml
<mapper namespace="kr.sy.kwag2.core.mapper.UserMapper">
  <select id="selectById" resultType="UserVO">
    SELECT user_id, username, email
    FROM public.users
    WHERE user_id = #{id}
  </select>
  
  <insert id="insert" useGeneratedKeys="true" keyProperty="userId">
    INSERT INTO public.users (username, email) 
    VALUES (#{username}, #{email})
  </insert>
</mapper>
```

## 🔄 마이그레이션 전략

### 1단계: 보조 DB 추가 및 테스트

```properties
# 보조 DB 추가
jdbc.secondary.url=jdbc:mysql://secondary-db:3306/kwag2_secondary
jdbc.secondary.driver=com.mysql.cj.jdbc.Driver
```

### 2단계: 데이터 동기화

```java
@Service
public class DataMigrationService {
    
    @Autowired
    private MultiDBManager multiDBManager;
    
    public void migratePrimaryToSecondary() {
        SqlSessionTemplate primary = multiDBManager.getDefaultSqlSessionTemplate();
        SqlSessionTemplate secondary = multiDBManager.getSqlSessionTemplate("secondary");
        
        // 마이그레이션 로직
        List<UserVO> users = primary.selectList("selectAllUsers");
        users.forEach(user -> secondary.insert("insertUser", user));
    }
}
```

### 3단계: 읽기 전용 복제 설정

```java
@Service
public class DataReadService {
    
    @Autowired
    private MultiDBManager multiDBManager;
    
    // 읽기는 보조 DB에서
    public List<UserVO> readUsersFromSecondary() {
        SqlSessionTemplate secondary = multiDBManager.getSqlSessionTemplate("secondary");
        return secondary.selectList("selectAllUsers");
    }
    
    // 쓰기는 주 DB에서
    public void saveUserToPrimary(UserVO user) {
        SqlSessionTemplate primary = multiDBManager.getDefaultSqlSessionTemplate();
        primary.insert("insertUser", user);
    }
}
```

## 🔌 커넥션 풀 설정

### HikariCP 최적화 설정

```properties
# 기본 커넥션 풀 설정
jdbc.pool.max=20              # 최대 연결 수
jdbc.pool.min=5               # 최소 연결 수

# 성능 튜닝 (production 환경)
jdbc.pool.max=50              # 높은 부하에 대비
jdbc.connection.timeout=10000 # 연결 타임아웃
```

## 📋 트러블슈팅

### 문제: "No SqlSessionTemplate found"
**해결**: 
1. spring-db.xml의 SqlSessionTemplate 빈 설정 확인
2. MultiDBManager의 SqlSessionTemplate 맵 설정 확인

### 문제: "Connection timeout"
**해결**:
1. 데이터베이스 서버가 실행 중인지 확인
2. JDBC URL과 포트 확인
3. 방화벽 설정 확인

### 문제: SQL 문법 오류
**해결**:
1. 데이터베이스별로 올바른 SQL 사용
2. 데이터 타입이 일치하는지 확인 (예: VARCHAR vs STRING)
3. 테이블/컬럼명 대소문자 확인 (Oracle은 대문자)

## 📚 추가 리소스

- [HikariCP 문서](https://github.com/brettwooldridge/HikariCP)
- [MyBatis 가이드](https://mybatis.org/mybatis-3/)
- [MySQL JDBC 드라이버](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [Oracle JDBC 드라이버](https://www.oracle.com/database/technologies/appdev/jdbc.html)
- [PostgreSQL JDBC 드라이버](https://jdbc.postgresql.org/)