# 모듈 개발 가이드

## 📚 소개

KWAG2는 모듈 기반 아키텍처를 제공합니다. 새로운 기능을 모듈로 개발하여 기존 시스템에 영향 없이 추가할 수 있습니다.

## 🎯 모듈의 개념

### 모듈이란?
- 특정 기능을 구현하는 독립적인 컴포넌트
- 다른 모듈과 독립적으로 활성화/비활성화 가능
- 테넌트별로 선택적으로 적용 가능

### 모듈의 종류
1. **Core 모듈**: 시스템 필수 모듈 (사용자, 권한 등)
2. **Custom 모듈**: 테넌트별 커스텀 기능 모듈
3. **Extension 모듈**: 기존 기능 확장 모듈

## 📝 모듈 개발 단계

### Step 1: 모듈 클래스 작성

```java
package kr.sy.kwag2.module.custom;

import kr.sy.kwag2.module.core.AbstractModule;
import org.springframework.stereotype.Component;

/**
 * 샘플 커스텀 모듈
 */
@Component
public class SampleModule extends AbstractModule {
    
    public SampleModule() {
        super(
            "sample-module",           // 모듈 ID
            "샘플 모듈",                // 모듈명
            "1.0.0",                   // 버전
            "테스트용 샘플 모듈입니다"  // 설명
        );
    }

    @Override
    public void initialize() {
        super.initialize();
        // 모듈 초기화 로직
        logger.info("SampleModule initialized");
    }

    @Override
    public void activate() {
        super.activate();
        // 활성화 시 실행할 로직
        logger.info("SampleModule activated");
    }

    @Override
    public void deactivate() {
        super.deactivate();
        // 비활성화 시 실행할 로직
        logger.info("SampleModule deactivated");
    }
}
```

### Step 2: 모듈 등록

Spring의 `@Component` 애노테이션으로 자동 등록:

```java
@Component
public class SampleModule extends AbstractModule {
    // ...
}
```

또는 수동 등록:

```java
@Component
public class ModuleInitializer {
    
    @Autowired
    private ModuleManager moduleManager;
    
    @PostConstruct
    public void initModules() {
        SampleModule sampleModule = new SampleModule();
        moduleManager.registerModule(sampleModule);
        moduleManager.activateModule("sample-module");
    }
}
```

### Step 3: 모듈별 Service/Controller 개발

```java
// SampleService.java
@Service
public class SampleService {
    public String doSomething() {
        String tenantId = MultiTenantContext.getTenantId();
        // 테넌트별 로직
        return "Result for tenant: " + tenantId;
    }
}
```

```java
// SampleController.java
@RestController
@RequestMapping("/api/v1/sample")
public class SampleController extends BaseController {
    
    @Autowired
    private SampleService sampleService;
    
    @GetMapping
    public ResponseEntity<ResponseVO<String>> getSample() {
        String result = sampleService.doSomething();
        return ResponseEntity.ok(success(result));
    }
}
```

### Step 4: 모듈별 MyBatis Mapper 작성

```java
// SampleMapper.java
@Mapper
public interface SampleMapper extends BaseMapper<SampleVO> {
    // 추가 메서드
}
```

```xml
<!-- mapper/mysql/SampleMapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="kr.sy.kwag2.module.custom.sample.mapper.SampleMapper">
    <!-- 쿼리 작성 -->
</mapper>
```

## 🔌 모듈 활성화/비활성화

### 전체 모듈 활성화
```java
moduleManager.activateModule("sample-module");
```

### 테넌트별 모듈 활성화
```java
moduleManager.enableModuleForTenant("sample-module", "tenant-001");
```

### 테넌트에 대해 활성화된 모듈 조회
```java
List<IModule> modules = moduleManager.getEnabledModulesForTenant("tenant-001");
```

## 📦 모듈 구조 예제

```
kwag2/
├── src/main/java/kr/sy/kwag2/
│   └── module/
│       └── custom/
│           └── invoice/              # Invoice 모듈
│               ├── InvoiceModule.java
│               ├── controller/
│               │   └── InvoiceController.java
│               ├── service/
│               │   ├── InvoiceService.java
│               │   └── impl/
│               │       └── InvoiceServiceImpl.java
│               ├── mapper/
│               │   └── InvoiceMapper.java
│               └── vo/
│                   └── InvoiceVO.java
├── src/main/resources/
│   └── mapper/
│       ├── mysql/
│       │   └── InvoiceMapper.xml
│       └── oracle/
│           └── InvoiceMapper.xml
```

## 🔄 모듈 생명주기

```
┌─────────────────────────────────────┐
│      ModuleManager.registerModule   │
└──────────────────┬──────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  IModule.initialize()│
        └──────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  ModuleManager       │
        │  stores module       │
        └──────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  IModule.activate()  │ (필요시)
        └──────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  Module Running      │
        └──────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │ IModule.deactivate() │ (필요시)
        └──────────────────────┘
                   │
                   ↓
┌──────────────────────────────────────┐
│ ModuleManager.unregisterModule       │
└──────────────────┬───────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  IModule.destroy()   │
        └──────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │  Module Removed      │
        └──────────────────────┘
```

## 📋 체크리스트

모듈을 개발할 때 다음을 확인하세요:

- [ ] 모듈 클래스가 `AbstractModule`을 상속
- [ ] 모듈 ID가 고유한지 확인
- [ ] Service 클래스가 `@Service`로 등록됨
- [ ] Controller가 `BaseController`를 상속
- [ ] Mapper가 `@Mapper`로 등록됨
- [ ] MyBatis XML이 올바른 경로에 있음
- [ ] 테넌트 컨텍스트 사용 확인
- [ ] 로깅이 적절히 추가됨
- [ ] API에 Swagger 애노테이션 추가

## 🧪 모듈 테스트

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleModuleTest {
    
    @Autowired
    private ModuleManager moduleManager;
    
    @Test
    public void testModuleActivation() {
        moduleManager.activateModule("sample-module");
        IModule module = moduleManager.getModule("sample-module");
        assertTrue(module.isActive());
    }
    
    @Test
    public void testTenantModuleEnabling() {
        moduleManager.enableModuleForTenant("sample-module", "tenant-001");
        IModule module = moduleManager.getModule("sample-module");
        assertTrue(module.isEnabledForTenant("tenant-001"));
    }
}
```

## 💡 베스트 프랙티스

1. **모듈 ID는 하이픈 사용**: `sample-module` (snake_case보다 좋음)
2. **버전 관리**: Semantic versioning 사용 (1.0.0)
3. **로깅 활용**: 모듈 초기화/활성화 시점에 로그 남기기
4. **에러 처리**: 모듈 활성화 실패 시 예외 처리
5. **문서화**: 모듈 기능을 README로 문서화
6. **테넌트 인식**: MultiTenantContext 활용하여 테넌트별 처리
7. **의존성 주입**: Spring의 @Autowired 활용

## ❓ FAQ

**Q: 모듈을 동적으로 로드할 수 있나?**
A: 네, `ModuleManager.registerModule()`을 통해 런타임에 모듈을 등록할 수 있습니다.

**Q: 모듈이 다른 모듈에 의존할 수 있나?**
A: 네, Spring의 의존성 주입을 활용하여 모듈 간 의존성을 관리할 수 있습니다.

**Q: 테넌트별로 다른 모듈을 사용할 수 있나?**
A: 네, `moduleManager.enableModuleForTenant()` / `disableModuleForTenant()`로 관리합니다.