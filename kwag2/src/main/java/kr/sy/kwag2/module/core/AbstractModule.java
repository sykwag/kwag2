package kr.sy.kwag2.module.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 모듈의 추상 기본 클래스
 * 모든 모듈이 상속받아 구현해야 함
 */
public abstract class AbstractModule implements IModule {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected String moduleId;
    protected String moduleName;
    protected String version;
    protected String description;
    protected boolean active = false;
    
    // 테넌트별 활성화 상태
    protected Map<String, Boolean> tenantEnabledMap = new HashMap<>();

    public AbstractModule(String moduleId, String moduleName, String version, String description) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.version = version;
        this.description = description;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void initialize() {
        logger.info("Module {} (v{}) initializing...", moduleName, version);
    }

    @Override
    public void activate() {
        this.active = true;
        logger.info("Module {} activated", moduleName);
    }

    @Override
    public void deactivate() {
        this.active = false;
        logger.info("Module {} deactivated", moduleName);
    }

    @Override
    public void destroy() {
        logger.info("Module {} destroying...", moduleName);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isEnabledForTenant(String tenantId) {
        return tenantEnabledMap.getOrDefault(tenantId, true);
    }

    /**
     * 테넌트별 모듈 활성화 설정
     */
    public void setEnabledForTenant(String tenantId, boolean enabled) {
        tenantEnabledMap.put(tenantId, enabled);
        logger.debug("Module {} for tenant {} set to {}", moduleName, tenantId, enabled);
    }

    @Override
    public String toString() {
        return "AbstractModule{" +
                "moduleId='" + moduleId + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", version='" + version + '\'' +
                ", active=" + active +
                '}';
    }
}