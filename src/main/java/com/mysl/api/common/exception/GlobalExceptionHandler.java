package com.mysl.api.common.exception;

import com.mysl.api.common.lang.Result;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  // @ExceptionHandler(value = RuntimeException.class)

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Exception.class)
  public Object handler(Exception e) {
    String msg = e.getMessage();
    // e.printStackTrace();
    log.info(e);
    return Result.ret(-1, (msg != null && msg.length() > 31) ? msg.substring(0, 31) : msg);
  }
}
