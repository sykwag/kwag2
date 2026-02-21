package kr.sy.kwag2.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import kr.sy.kwag2.core.context.MultiTenantContext;
import kr.sy.kwag2.core.mapper.BaseMapper;
import kr.sy.kwag2.core.service.BaseService;

import java.util.List;

/**
 * 모든 ServiceImpl의 기본 클래스
 * 공통 CRUD 로직 구현
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 각 구현체에서 해당 Mapper를 반환해야 함
     */
    protected abstract BaseMapper<T> getMapper();

    @Override
    @Transactional(readOnly = true)
    public T findById(String id) {
        logger.debug("Finding entity by id: {} in tenant: {}", id, MultiTenantContext.getTenantId());
        return getMapper().selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        logger.debug("Finding all entities in tenant: {}", MultiTenantContext.getTenantId());
        return getMapper().selectAll();
    }

    @Override
    @Transactional
    public void create(T entity) {
        logger.debug("Creating entity in tenant: {}", MultiTenantContext.getTenantId());
        getMapper().insert(entity);
    }

    @Override
    @Transactional
    public void update(T entity) {
        logger.debug("Updating entity in tenant: {}", MultiTenantContext.getTenantId());
        getMapper().update(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        logger.debug("Deleting entity by id: {} in tenant: {}", id, MultiTenantContext.getTenantId());
        getMapper().delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        logger.debug("Counting entities in tenant: {}", MultiTenantContext.getTenantId());
        return getMapper().count();
    }
}