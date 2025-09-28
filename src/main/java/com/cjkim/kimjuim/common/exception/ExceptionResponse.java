package com.cjkim.kimjuim.common.exception;

public record ExceptionResponse(
        int exceptionCode,
        String message
) {
}
