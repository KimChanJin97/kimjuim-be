package com.cjkim.kimjuim.question;

import com.cjkim.kimjuim.common.exception.ExceptionInfo;
import com.cjkim.kimjuim.common.exception.Status;

public enum QuestionExceptionInfo implements ExceptionInfo {

    MESSAGE_FAILED(Status.SERVER_ERROR, 1001, "메일 전송 실패")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    QuestionExceptionInfo(Status status, int exceptionCode, String message) {
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
