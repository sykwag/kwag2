package kr.sy.kwag2.core.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.sy.kwag2.core.context.MultiTenantContext;

/**
 * 멀티테넌트 컨텍스트 필터
 * 요청에서 테넌트 ID를 추출하고 ThreadLocal에 저장
 */
public class MultiTenantContextFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantContextFilter.class);
    
    private String tenantHeaderName = "X-Tenant-ID";
    private String tenantCookieName = "tenantId";

    @Override
    public void init(FilterConfig config) throws ServletException {
        String headerName = config.getInitParameter("tenantHeaderName");
        if (headerName != null) {
            this.tenantHeaderName = headerName;
        }
        String cookieName = config.getInitParameter("tenantCookieName");
        if (cookieName != null) {
            this.tenantCookieName = cookieName;
        }
        logger.info("MultiTenantContextFilter initialized with header: {}, cookie: {}",
                tenantHeaderName, tenantCookieName);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        try {
            // 테넌트 ID 추출
            String tenantId = extractTenantId(httpRequest);
            logger.debug("Extracted tenantId: {}", tenantId);
            
            // ThreadLocal에 설정
            MultiTenantContext.setTenantId(tenantId);
            
            // 다음 필터/요청 처리
            chain.doFilter(request, response);
        } finally {
            // 컨텍스트 정리
            MultiTenantContext.clear();
        }
    }

    @Override
    public void destroy() {
        logger.info("MultiTenantContextFilter destroyed");
    }

    /**
     * 요청에서 테넌트 ID 추출
     * 우선순위: Header > Cookie > Default
     */
    private String extractTenantId(HttpServletRequest request) {
        // 1. Header에서 추출
        String tenantId = request.getHeader(tenantHeaderName);
        if (tenantId != null && !tenantId.isEmpty()) {
            return tenantId;
        }

        // 2. Cookie에서 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tenantCookieName.equals(cookie.getName())) {
                    tenantId = cookie.getValue();
                    if (tenantId != null && !tenantId.isEmpty()) {
                        return tenantId;
                    }
                }
            }
        }

        // 3. URI 경로에서 추출 (예: /tenant/tenantName/...)
        String path = request.getRequestURI();
        if (path.startsWith("/tenant/")) {
            String[] parts = path.split("/");
            if (parts.length > 2) {
                return parts[2];
            }
        }

        // 기본값 반환
        return "default";
    }
}