package kr.sy.kwag2.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.sy.kwag2.common.vo.UserVO;

/**
 * 사용자 Mapper
 * MyBatis 매퍼 인터페이스
 */
@Mapper
public interface UserMapper extends BaseMapper<UserVO> {
    /**
     * 사용자명으로 사용자 조회
     */
    UserVO selectByUsername(@Param("username") String username);

    /**
     * 이메일로 사용자 조회
     */
    UserVO selectByEmail(@Param("email") String email);

    /**
     * 테넌트별 사용자 수 조회
     */
    int countByTenantId(@Param("tenantId") String tenantId);
}