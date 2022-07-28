package com.mysl.api.common.lang;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Result {
    private static Object main(int code, String msg, Object data) {
        return data == null ? (msg == null ? new NoMsg(code) : new NoData(code, msg)) : new ExitData(code, msg, data);
    }

    public static Object ret(int code, String msg, Object data) {
        return main(code, msg, data);
    }

    public static Object ret(int code, Object data, String msg) {
        return main(code, msg, data);
    }

    public static Object ret(String msg, int code, Object data) {
        return main(code, msg, data);
    }

    public static Object ret(String msg, Object data, int code) {
        return main(code, msg, data);
    }

    public static Object ret(Object data, int code, String msg) {
        return main(code, msg, data);
    }

    public static Object ret(Object data, String msg, int code) {
        return main(code, msg, data);
    }

    public static Object ret(int code, String msg) {
        return main(code, msg, null);
    }

    public static Object ret(int code, Object data) {
        return main(code, null, data);
    }

    public static Object ret(String msg, int code) {
        return main(code, msg, null);
    }

    public static Object ret(String msg, Object data) {
        return main(0, msg, data);
    }

    public static Object ret(Object data, int code) {
        return main(code, null, data);
    }

    public static Object ret(Object data, String msg) {
        return main(0, msg, data);
    }

    public static Object ret(int code) {
        return main(code, null, null);
    }

    public static Object ret(String msg) {
        return main(0, msg, null);
    }

    public static Object ret(Object data) {
        return main(0, null, data);
    }

    public static Object ret() {
        return main(0, null, null);
    }

    public static ResponseEntity badRequest(String msg) {
        return ResponseEntity.badRequest().body(ret(HttpStatus.BAD_REQUEST.value(), msg));
    }

    public static ResponseEntity ok(Object data) {
        return ResponseEntity.ok(ret(HttpStatus.OK.value(), data));
    }

//  public static ResponseEntity ret(HttpStatus status, String msg) {
//    return ResponseEntity.status(status).body(ret(status.value(), msg));
//  }
}
