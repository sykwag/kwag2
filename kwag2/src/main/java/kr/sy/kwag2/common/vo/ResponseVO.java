package kr.sy.kwag2.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * 모든 API 응답의 표준 VO
 * 일관된 응답 포맷을 제공
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private T data;
    private String tenantId;
    private long timestamp;

    // ===== Constructors =====
    public ResponseVO() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseVO(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ResponseVO(String code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ===== Builder Pattern =====
    public static <T> ResponseVOBuilder<T> builder() {
        return new ResponseVOBuilder<>();
    }

    public static class ResponseVOBuilder<T> {
        private String code;
        private String message;
        private T data;
        private String tenantId;

        public ResponseVOBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public ResponseVOBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseVOBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseVOBuilder<T> tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public ResponseVO<T> build() {
            ResponseVO<T> responseVO = new ResponseVO<>();
            responseVO.code = this.code;
            responseVO.message = this.message;
            responseVO.data = this.data;
            responseVO.tenantId = this.tenantId;
            return responseVO;
        }
    }

    // ===== Getters & Setters =====
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", tenantId='" + tenantId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}