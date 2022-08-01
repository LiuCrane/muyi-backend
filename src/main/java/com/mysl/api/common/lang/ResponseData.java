package com.mysl.api.common.lang;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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
}
