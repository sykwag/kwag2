package kr.sy.kwag2.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 모든 Mapper의 기본 인터페이스
 * MyBatis Mapper 기반 데이터 접근 계층
 */
@Mapper
public interface BaseMapper<T> {
    /**
     * ID로 단일 조회
     */
    T selectById(String id);

    /**
     * 전체 조회
     */
    List<T> selectAll();

    /**
     * 삽입
     */
    int insert(T entity);

    /**
     * 수정
     */
    int update(T entity);

    /**
     * 삭제
     */
    int delete(String id);

    /**
     * 건수 조회
     */
    int count();
}