package com.mysl.api.service.impl;

import io.github.flyhero.easylog.function.ICustomFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Su
 * @date 2022/9/4
 */
@Component
public class OpGetClientIPFunction implements ICustomFunction {

    public static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    @Override
    public boolean executeBefore() {
        return false;
    }

    @Override
    public String functionName() {
        return "getClientIP";
    }

    @Override
    public String apply(String value) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (request == null) {
            return null;
        }
        String xfHeader = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
