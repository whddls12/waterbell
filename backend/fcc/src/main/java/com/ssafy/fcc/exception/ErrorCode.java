package com.ssafy.fcc.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {


    /* 200 OK : 성공 */
    SUCCESS(200, "OK", "요청에 성공하였습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT_VALUE(400, "BAD_REQUEST", "입력값이 올바르지 않습니다."),
    BAD_REQUEST(400, "BAD_REQUEST","잘못된 요청입니다."),
    INVALID_PARAMS(400, "InvalidParams", "필수데이터 누락, 또는 형식과 다른 데이터를 요청하셨습니다."),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    NON_LOGIN(401, "UNAUTHORIZED","인증이 필요하거나 유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "ExpiredToken", "토큰이 만료되었습니다."),
    INVALID_TOKEN(401, "InvalidToken", "토큰이 유효하지 않습니다."),
    UNAVAILABLE(401, "Unavailable", "회원가입이 완료되지 않은 사용자입니다."),

    /* 403 FORBIDDEN : 접근권한 없음 */
    ACCESS_DENIED(403, "FORBIDDEN","접근이 거부되었습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(404, "NOT_FOUND","해당 유저 정보를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(404, "NOT_FOUND","해당 정보를 찾을 수 없습니다."),
    NOT_FOUND(404, "NotFound", "존재하지 않는 데이터입니다."),

    /* 405 METHOD_NOT_ALLOWED : 지원하지 않는 HTTP Method */
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED","허용되지 않은 요청입니다."),

    /* 409 CONFLICT : 데이터 중복 */
    DUPLICATE_RESOURCE(409, "CONFLICT","데이터가 이미 존재합니다"),
    CONFLICT(409, "Conflict", "데이터가 충돌되었습니다."),

    /* 500 INTERNAL_SERVER_ERROR */
    SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "예기치 못한 오류가 발생하였습니다.");




    ErrorCode (int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private int status;
    private String code;
    private String message;

    public static ErrorCode valueOfCode(String errorCode) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(errorCode))
                .findAny()
                .orElse(null);
    }

}