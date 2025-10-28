package com.cjkim.kimjuim.redis;

import com.cjkim.kimjuim.common.exception.ExceptionInfo;
import com.cjkim.kimjuim.common.exception.Status;

public enum RequestRateLimiterExceptionInfo implements ExceptionInfo {

    BLACKLIST_ENROLLED(Status.FORBIDDEN, 4001, "과도한 요청으로 블랙리스트 처리되었습니다.")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    RequestRateLimiterExceptionInfo(Status status, int exceptionCode, String message) {
        this.status = status;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public int exceptionCode() {
        return exceptionCode;
    }

    @Override
    public String message() {
        return message;
    }
}
