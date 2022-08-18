package com.mysl.api.common.lang;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan Su
 * @date 2022/8/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

    private int code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ResponseData<T> ok() {
        return generator(HttpStatus.OK.value(), "", null);
    }

    public static <T> ResponseData<T> ok(T data) {
        return generator(HttpStatus.OK.value(), "", data);
    }

    public static <T> ResponseData<T> generator(int code, String message, T data) {
        // 创建响应标准格式对象
        ResponseData<T> responseData = new ResponseData<T>();
        responseData.setCode(code);
        responseData.setMsg(message);
        responseData.setData(data);
        return responseData;
    }

    public static <T> ResponseData<T> generator(int bizCode) {
        // 创建响应标准格式对象
        ResponseData<T> responseData = new ResponseData<T>();
        responseData.setCode(bizCode);
        responseData.setMsg(CODE_MAP.get(bizCode));
        return responseData;
    }

    public static final Map<Integer, String> CODE_MAP = Map.of(
            400101, "token丢失或无效",
            400102, "用户名或密码错误",
            400103, "您的位置与注册地址偏差较大，请在指定位置登录！",
            400104, "当前处于禁止登录时间，将退出应用。"
    );
}
