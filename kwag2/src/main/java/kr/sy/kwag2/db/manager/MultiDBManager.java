package kr.sy.kwag2.db.manager;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 다중 데이터베이스 관리자
 * 여러 개의 SqlSessionTemplate을 관리하고 선택적으로 제공
 */
@Component
public class MultiDBManager {
    private Map<String, SqlSessionTemplate> sqlSessionTemplates = new HashMap<>();

    /**
     * SqlSessionTemplate 등록
     */
    public void registerSqlSessionTemplate(String dbKey, SqlSessionTemplate sqlSessionTemplate) {
        sqlSessionTemplates.put(dbKey, sqlSessionTemplate);
    }

    /**
     * SqlSessionTemplate 조회
     */
    public SqlSessionTemplate getSqlSessionTemplate(String dbKey) {
        SqlSessionTemplate template = sqlSessionTemplates.get(dbKey);
        if (template == null) {
            // 기본 데이터베이스 반환
            template = sqlSessionTemplates.get("default");
            if (template == null) {
                throw new IllegalArgumentException("No SqlSessionTemplate found for key: " + dbKey);
            }
        }
        return template;
    }

    /**
     * 기본 SqlSessionTemplate 조회
     */
    public SqlSessionTemplate getDefaultSqlSessionTemplate() {
        return getSqlSessionTemplate("default");
    }

    /**
     * 등록된 모든 DB 키 조회
     */
    public java.util.Set<String> getAvailableDBKeys() {
        return sqlSessionTemplates.keySet();
    }

    /**
     * SqlSessionTemplate 맵 설정 (스프링 빈 생성 시)
     */
    public void setSqlSessionTemplates(Map<String, SqlSessionTemplate> sqlSessionTemplates) {
        this.sqlSessionTemplates = sqlSessionTemplates;
    }
}