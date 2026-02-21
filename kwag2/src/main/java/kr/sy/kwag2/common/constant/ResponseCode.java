package kr.sy.kwag2.common.constant;

/**
 * 응답 코드 열거형
 */
public enum ResponseCode {
    SUCCESS("0000", "성공"),
    BAD_REQUEST("4000", "잘못된 요청"),
    UNAUTHORIZED("4010", "인증되지 않음"),
    FORBIDDEN("4030", "접근 권한 없음"),
    NOT_FOUND("4040", "리소스를 찾을 수 없음"),
    INTERNAL_SERVER_ERROR("5000", "서버 내부 오류"),
    SERVICE_UNAVAILABLE("5030", "서비스 이용불가");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}