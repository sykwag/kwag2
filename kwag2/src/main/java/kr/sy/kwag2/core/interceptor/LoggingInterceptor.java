package kr.sy.kwag2.core.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.sy.kwag2.core.context.MultiTenantContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 요청/응답 로깅 인터셉터
 */
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        logger.info(">>> Request [{}] {} - Tenant: {}, Method: {}",
                request.getRequestID(),
                request.getRequestURI(),
                MultiTenantContext.getTenantId(),
                request.getMethod());
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 필요시 처리
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        
        if (ex != null) {
            logger.error("<<< Response [{}] {} - Status: {}, Duration: {}ms, Error: {}",
                    request.getRequestID(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    ex.getMessage());
        } else {
            logger.info("<<< Response [{}] {} - Status: {}, Duration: {}ms",
                    request.getRequestID(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
        }
    }
}