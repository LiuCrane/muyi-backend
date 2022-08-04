package com.mysl.api.common.lang;

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

}
