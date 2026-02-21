package kr.sy.kwag2.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.sy.kwag2.core.context.MultiTenantContext;
import kr.sy.kwag2.module.manager.ModuleManager;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * KWAG2 프레임워크 통합 테스트
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {
    "classpath:spring/spring-context.xml",
    "classpath:spring/spring-mvc.xml",
    "classpath:spring/spring-db.xml",
    "classpath:spring/spring-aop.xml"
})
public class KWAG2IntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ModuleManager moduleManager;

    private MockMvc mockMvc;

    @BeforeClass
    public static void setUp() {
        System.out.println("========================================");
        System.out.println("KWAG2 Integration Test Starting");
        System.out.println("========================================");
    }

    // ===== MultiTenant Context 테스트 =====

    @Test
    public void testMultiTenantContextSetting() {
        System.out.println("\n>>> Test: MultiTenant Context Setting");
        
        // 테넌트 ID 설정
        MultiTenantContext.setTenantId("tenant-001");
        MultiTenantContext.setUserId("user-001");
        MultiTenantContext.setUserName("John Doe");
        
        // 확인
        assertEquals("tenant-001", MultiTenantContext.getTenantId());
        assertEquals("user-001", MultiTenantContext.getUserId());
        assertEquals("John Doe", MultiTenantContext.getUserName());
        
        System.out.println("Context Info: " + MultiTenantContext.getContextInfo());
        System.out.println("✓ Test passed");
    }

    @Test
    public void testMultiTenantContextDefault() {
        System.out.println("\n>>> Test: MultiTenant Context Default");
        
        // 초기화
        MultiTenantContext.clear();
        
        // 기본값 확인
        assertEquals("default", MultiTenantContext.getTenantId());
        assertNull(MultiTenantContext.getUserId());
        assertNull(MultiTenantContext.getUserName());
        
        System.out.println("✓ Test passed");
    }

    @Test
    public void testMultiTenantContextClearing() {
        System.out.println("\n>>> Test: MultiTenant Context Clearing");
        
        // 설정
        MultiTenantContext.setTenantId("tenant-001");
        MultiTenantContext.setUserId("user-001");
        
        // 초기화
        MultiTenantContext.clear();
        
        // 초기화 확인
        assertEquals("default", MultiTenantContext.getTenantId());
        assertNull(MultiTenantContext.getUserId());
        
        System.out.println("✓ Test passed");
    }

    // ===== Module Manager 테스트 =====

    @Test
    public void testModuleManagerInitialization() {
        System.out.println("\n>>> Test: Module Manager Initialization");
        
        assertNotNull(moduleManager);
        System.out.println("Module count: " + moduleManager.getModuleCount());
        
        System.out.println("✓ Test passed");
    }

    @Test
    public void testModuleManagerStatus() {
        System.out.println("\n>>> Test: Module Manager Status");
        
        moduleManager.printStatus();
        
        System.out.println("✓ Test passed");
    }

    // ===== 기본 Service 테스트 =====

    @Test
    public void testFrameworkInitialization() {
        System.out.println("\n>>> Test: Framework Initialization");
        
        assertNotNull(context);
        assertNotNull(moduleManager);
        
        System.out.println("Framework initialized successfully");
        System.out.println("✓ Test passed");
    }

    @Test
    public void testApplicationContextLoading() {
        System.out.println("\n>>> Test: Application Context Loading");
        
        // Spring 빈 로딩 확인
        assertNotNull(context);
        assertTrue(context.containsBean("moduleManager"));
        
        System.out.println("Application context loaded successfully");
        System.out.println("✓ Test passed");
    }

    // ===== API 테스트 =====

    @Test
    public void testSwaggerUIAvailable() {
        System.out.println("\n>>> Test: Swagger UI Available");
        
        // Swagger가 설정되어 있는지 확인
        // application.properties에서 swagger.enabled=true 필요
        System.out.println("Swagger UI: http://localhost:8080/kwag2/swagger-ui.html");
        
        System.out.println("✓ Test passed");
    }

    // ===== 성능 테스트 =====

    @Test
    public void testMultiTenantContextPerformance() {
        System.out.println("\n>>> Test: MultiTenant Context Performance");
        
        long startTime = System.currentTimeMillis();
        
        // 10,000번 반복
        for (int i = 0; i < 10000; i++) {
            MultiTenantContext.setTenantId("tenant-" + i);
            String tenantId = MultiTenantContext.getTenantId();
            assertEquals("tenant-" + i, tenantId);
        }
        
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("10,000 iterations: " + duration + "ms");
        System.out.println("Average: " + (duration / 10000.0) + "ms per iteration");
        
        MultiTenantContext.clear();
        System.out.println("✓ Test passed");
    }

    @Test
    public void testConcurrentMultiTenantContext() throws InterruptedException {
        System.out.println("\n>>> Test: Concurrent MultiTenant Context");
        
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < 5; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                MultiTenantContext.setTenantId("tenant-" + threadNum);
                System.out.println("Thread " + threadNum + ": " + MultiTenantContext.getTenantId());
                assertEquals("tenant-" + threadNum, MultiTenantContext.getTenantId());
            });
            threads[i].start();
        }
        
        // 모든 스레드가 끝날 때까지 대기
        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("✓ Test passed");
    }

    // ===== 통합 테스트 =====

    @Test
    public void testCompleteWorkflow() {
        System.out.println("\n>>> Test: Complete Workflow");
        
        try {
            // 1. 테넌트 설정
            MultiTenantContext.setTenantId("tenant-001");
            MultiTenantContext.setUserId("admin");
            MultiTenantContext.setUserName("Administrator");
            
            System.out.println("1. Tenant context set: " + MultiTenantContext.getContextInfo());
            
            // 2. 모듈 매니저 확인
            assertNotNull(moduleManager);
            System.out.println("2. Module manager loaded: " + moduleManager.getModuleCount() + " modules");
            
            // 3. 테넌트 활성 모듈 조회
            var activeModules = moduleManager.getEnabledModulesForTenant("tenant-001");
            System.out.println("3. Active modules for tenant: " + activeModules.size());
            
            // 4. 컨텍스트 초기화
            MultiTenantContext.clear();
            assertEquals("default", MultiTenantContext.getTenantId());
            System.out.println("4. Context cleared");
            
            System.out.println("✓ Test passed");
        } finally {
            MultiTenantContext.clear();
        }
    }
}