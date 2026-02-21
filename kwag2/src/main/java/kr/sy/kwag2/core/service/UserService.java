package kr.sy.kwag2.core.service;

import kr.sy.kwag2.common.vo.UserVO;

/**
 * 사용자 서비스 인터페이스
 */
public interface UserService extends BaseService<UserVO> {
    /**
     * 사용자명으로 사용자 조회
     */
    UserVO findByUsername(String username);

    /**
     * 이메일로 사용자 조회
     */
    UserVO findByEmail(String email);

    /**
     * 테넌트별 사용자 수 조회
     */
    int countByTenantId(String tenantId);
}