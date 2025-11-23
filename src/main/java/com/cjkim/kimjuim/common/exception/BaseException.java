package com.cjkim.kimjuim.common.exception;


public class BaseException extends RuntimeException {

    private final ExceptionInfo exceptionInfo;

    protected BaseException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.message());
        this.exceptionInfo = exceptionInfo;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }
}
