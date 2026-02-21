package kr.sy.kwag2.module.core;

/**
 * 모듈 인터페이스
 * 모든 모듈이 구현해야 하는 기본 계약
 */
public interface IModule {
    /**
     * 모듈 ID 조회
     */
    String getModuleId();

    /**
     * 모듈명 조회
     */
    String getModuleName();

    /**
     * 모듈 버전 조회
     */
    String getVersion();

    /**
     * 모듈 설명
     */
    String getDescription();

    /**
     * 모듈 초기화
     */
    void initialize();

    /**
     * 모듈 활성화
     */
    void activate();

    /**
     * 모듈 비활성화
     */
    void deactivate();

    /**
     * 모듈 정리
     */
    void destroy();

    /**
     * 모듈이 활성화 상태인지 확인
     */
    boolean isActive();

    /**
     * 테넌트별 모듈 활성화 여부 확인
     */
    boolean isEnabledForTenant(String tenantId);
}