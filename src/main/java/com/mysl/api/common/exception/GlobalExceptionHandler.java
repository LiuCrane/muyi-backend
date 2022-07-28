package com.mysl.api.common.exception;

import com.mysl.api.common.lang.Result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // @ExceptionHandler(value = RuntimeException.class)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Object handler(Exception e) {
        String msg = e.getMessage();
        // e.printStackTrace();
        log.info(e);
        return Result.ret(HttpStatus.INTERNAL_SERVER_ERROR.value(), (msg != null && msg.length() > 31) ? msg.substring(0, 31) : msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return Result.badRequest(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity requestParameterExceptionHandler(MethodArgumentTypeMismatchException e) {
        return Result.badRequest("参数格式错误");
    }

}
