package com.cjkim.kimjuim.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleApplicationException(BaseException e) {
        log.warn(e.getMessage(), e);
        ExceptionInfo exceptionInfo = e.getExceptionInfo();
        ExceptionStatus exceptionStatus = ExceptionStatus.from(exceptionInfo.status());
        return ResponseEntity
                .status(exceptionStatus.getHttpStatus())
                .body(new ExceptionResponse(exceptionInfo.exceptionCode(), exceptionInfo.message()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNonApplicationException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(0000, e.getMessage()));
    }
}
