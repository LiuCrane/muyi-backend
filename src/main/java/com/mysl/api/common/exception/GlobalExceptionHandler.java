package com.mysl.api.common.exception;

import com.mysl.api.common.lang.ResponseData;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseData handler(Exception e) {
        String msg = e.getMessage();
        log.info("GlobalExceptionHandler: ", e);
//        return Result.ret(HttpStatus.INTERNAL_SERVER_ERROR.value(), (msg != null && msg.length() > 31) ? msg.substring(0, 31) : msg);
        return ResponseData.generator(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务错误", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseData requestParameterExceptionHandler(MethodArgumentTypeMismatchException e) {
        return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "参数格式错误", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseData httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "请求内容为空", null);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return ResponseData.generator(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式错误", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseData authenticationExceptionHandler(AuthenticationException e) {
        log.error("authenticationExceptionHandler: ", e);
        return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "用户名或密码错误", null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ResponseData serviceErrorHandler(ServiceException e) {
        return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseData accessDeniedExceptionHandler(AccessDeniedException e) {
        return ResponseData.generator(HttpStatus.FORBIDDEN.value(), "无权限访问", null);
    }

}
