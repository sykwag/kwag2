package kr.sy.kwag2.module.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.sy.kwag2.module.core.IModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 모듈 매니저
 * 모듈의 생명주기를 관리하고 필요한 모듈을 제공
 */
@Component
public class ModuleManager {
    private static final Logger logger = LoggerFactory.getLogger(ModuleManager.class);
    
    // 모듈 저장소
    private Map<String, IModule> modules = new HashMap<>();

    /**
     * 모듈 등록
     */
    public void registerModule(IModule module) {
        if (modules.containsKey(module.getModuleId())) {
            logger.warn("Module {} already registered", module.getModuleId());
            return;
        }
        
        module.initialize();
        modules.put(module.getModuleId(), module);
        logger.info("Module {} registered successfully", module.getModuleId());
    }

    /**
     * 모듈 등록 해제
     */
    public void unregisterModule(String moduleId) {
        IModule module = modules.remove(moduleId);
        if (module != null) {
            module.deactivate();
            module.destroy();
            logger.info("Module {} unregistered", moduleId);
        }
    }

    /**
     * 모듈 조회
     */
    public IModule getModule(String moduleId) {
        return modules.get(moduleId);
    }

    /**
     * 모듈 활성화
     */
    public void activateModule(String moduleId) {
        IModule module = modules.get(moduleId);
        if (module != null) {
            module.activate();
        }
    }

    /**
     * 모듈 비활성화
     */
    public void deactivateModule(String moduleId) {
        IModule module = modules.get(moduleId);
        if (module != null) {
            module.deactivate();
        }
    }

    /**
     * 테넌트에 대한 모듈 활성화
     */
    public void enableModuleForTenant(String moduleId, String tenantId) {
        IModule module = modules.get(moduleId);
        if (module != null) {
            module.setEnabledForTenant(tenantId, true);
            logger.info("Module {} enabled for tenant {}", moduleId, tenantId);
        }
    }

    /**
     * 테넌트에 대한 모듈 비활성화
     */
    public void disableModuleForTenant(String moduleId, String tenantId) {
        IModule module = modules.get(moduleId);
        if (module != null) {
            module.setEnabledForTenant(tenantId, false);
            logger.info("Module {} disabled for tenant {}", moduleId, tenantId);
        }
    }

    /**
     * 테넌트에 대해 활성화된 모든 모듈 조회
     */
    public List<IModule> getEnabledModulesForTenant(String tenantId) {
        return modules.values().stream()
                .filter(IModule::isActive)
                .filter(m -> m.isEnabledForTenant(tenantId))
                .collect(Collectors.toList());
    }

    /**
     * 모든 등록된 모듈 조회
     */
    public List<IModule> getAllModules() {
        return List.copyOf(modules.values());
    }

    /**
     * 활성화된 모든 모듈 조회
     */
    public List<IModule> getActiveModules() {
        return modules.values().stream()
                .filter(IModule::isActive)
                .collect(Collectors.toList());
    }

    /**
     * 등록된 모듈 개수
     */
    public int getModuleCount() {
        return modules.size();
    }

    /**
     * 모듈 존재 여부 확인
     */
    public boolean hasModule(String moduleId) {
        return modules.containsKey(moduleId);
    }

    /**
     * 매니저 상태 출력 (디버깅용)
     */
    public void printStatus() {
        logger.info("=== Module Manager Status ===");
        logger.info("Total modules: {}", modules.size());
        modules.forEach((id, module) -> {
            logger.info("- {} (v{}) - Active: {}", 
                    module.getModuleName(), 
                    module.getVersion(), 
                    module.isActive());
        });
    }
}