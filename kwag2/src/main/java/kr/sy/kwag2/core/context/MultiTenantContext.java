package kr.sy.kwag2.core.context;

/**
 * 멀티테넌트 컨텍스트
 * 각 요청에 대해 테넌트 정보를 관리하는 ThreadLocal 기반 컨텍스트
 */
public class MultiTenantContext {
    private static final ThreadLocal<String> tenantIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userNameHolder = new ThreadLocal<>();

    /**
     * 테넌트 ID 설정
     */
    public static void setTenantId(String tenantId) {
        tenantIdHolder.set(tenantId);
    }

    /**
     * 테넌트 ID 조회
     */
    public static String getTenantId() {
        String tenantId = tenantIdHolder.get();
        return tenantId != null ? tenantId : "default";
    }

    /**
     * 사용자 ID 설정
     */
    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    /**
     * 사용자 ID 조회
     */
    public static String getUserId() {
        return userIdHolder.get();
    }

    /**
     * 사용자명 설정
     */
    public static void setUserName(String userName) {
        userNameHolder.set(userName);
    }

    /**
     * 사용자명 조회
     */
    public static String getUserName() {
        return userNameHolder.get();
    }

    /**
     * 컨텍스트 초기화 (요청 완료 후 호출)
     */
    public static void clear() {
        tenantIdHolder.remove();
        userIdHolder.remove();
        userNameHolder.remove();
    }

    /**
     * 현재 컨텍스트 정보 출력 (디버깅용)
     */
    public static String getContextInfo() {
        return String.format("TenantId: %s, UserId: %s, UserName: %s",
                getTenantId(), getUserId(), getUserName());
    }
}