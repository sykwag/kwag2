package kr.sy.kwag2.core.service;

import java.util.List;

/**
 * 모든 서비스의 기본 인터페이스
 * 공통 CRUD 메서드 정의
 */
public interface BaseService<T> {
    /**
     * 단일 조회
     */
    T findById(String id);

    /**
     * 전체 조회
     */
    List<T> findAll();

    /**
     * 생성
     */
    void create(T entity);

    /**
     * 수정
     */
    void update(T entity);

    /**
     * 삭제
     */
    void delete(String id);

    /**
     * 건수 조회
     */
    int count();
}