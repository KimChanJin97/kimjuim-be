package com.cjkim.kimjuim.redis;

import com.cjkim.kimjuim.common.exception.BaseException;
import com.cjkim.kimjuim.common.exception.ExceptionInfo;

public class RequestRateLimiterException extends BaseException {

    public RequestRateLimiterException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
}