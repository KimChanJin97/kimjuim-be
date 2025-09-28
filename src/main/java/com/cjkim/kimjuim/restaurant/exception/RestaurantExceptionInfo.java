package com.cjkim.kimjuim.restaurant.exception;

import com.cjkim.kimjuim.common.exception.ExceptionInfo;
import com.cjkim.kimjuim.common.exception.Status;

public enum RestaurantExceptionInfo implements ExceptionInfo {

    RESTAURANT_NOT_FOUND(Status.NOT_FOUND, 4001, "음식점이 없습니다.")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    RestaurantExceptionInfo(Status status, int exceptionCode, String message) {
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
