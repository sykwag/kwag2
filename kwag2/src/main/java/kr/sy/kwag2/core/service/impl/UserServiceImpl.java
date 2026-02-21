package kr.sy.kwag2.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.sy.kwag2.common.vo.UserVO;
import kr.sy.kwag2.core.mapper.BaseMapper;
import kr.sy.kwag2.core.mapper.UserMapper;
import kr.sy.kwag2.core.service.UserService;

/**
 * 사용자 서비스 구현
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserVO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected BaseMapper<UserVO> getMapper() {
        return userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        return userMapper.selectByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByTenantId(String tenantId) {
        logger.debug("Counting users for tenant: {}", tenantId);
        return userMapper.countByTenantId(tenantId);
    }
}