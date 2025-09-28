package com.cjkim.kimjuim.common.exception;

public interface ExceptionInfo {

    Status status();

    int exceptionCode();

    String message();
}
