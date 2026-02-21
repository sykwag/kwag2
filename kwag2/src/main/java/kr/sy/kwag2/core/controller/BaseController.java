package kr.sy.kwag2.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.sy.kwag2.common.constant.ResponseCode;
import kr.sy.kwag2.common.vo.ResponseVO;
import kr.sy.kwag2.core.context.MultiTenantContext;

/**
 * 모든 컨트롤러의 기본 클래스
 * 공통 예외 처리, 응답 포맷팅 등을 담당
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 성공 응답 반환
     */
    protected <T> ResponseVO<T> success(T data) {
        return ResponseVO.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .tenantId(MultiTenantContext.getTenantId())
                .build();
    }

    /**
     * 성공 응답 반환 (데이터 없음)
     */
    protected ResponseVO<Void> success() {
        return ResponseVO.<Void>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .tenantId(MultiTenantContext.getTenantId())
                .build();
    }

    /**
     * 성공 응답 반환 (메시지 지정)
     */
    protected <T> ResponseVO<T> success(T data, String message) {
        return ResponseVO.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .tenantId(MultiTenantContext.getTenantId())
                .build();
    }

    /**
     * 에러 응답 반환
     */
    protected ResponseVO<Void> error(ResponseCode responseCode) {
        return ResponseVO.<Void>builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .tenantId(MultiTenantContext.getTenantId())
                .build();
    }

    /**
     * 에러 응답 반환 (커스텀 메시지)
     */
    protected ResponseVO<Void> error(ResponseCode responseCode, String message) {
        return ResponseVO.<Void>builder()
                .code(responseCode.getCode())
                .message(message)
                .tenantId(MultiTenantContext.getTenantId())
                .build();
    }

    /**
     * 일반 예외 처리
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVO<Void> handleException(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return error(ResponseCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * 유효성 검사 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseVO<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument exception: {}", ex.getMessage());
        return error(ResponseCode.BAD_REQUEST, ex.getMessage());
    }
}