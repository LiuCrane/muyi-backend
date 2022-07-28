package com.mysl.api.interceptor;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.mysl.api.common.lang.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 拦截器
 * @author: nxq email: niuxiangqian163@163.com
 * @createDate: 2020/12/22 3:26 下午
 * @updateUser: nxq email: niuxiangqian163@163.com
 * @updateDate: 2020/12/22 3:26 下午
 * @updateRemark:
 * @version: 1.0
 **/
@Log4j2
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Value("${mysl.jwt.key}")
    private String key;

    /**
     * 访问接口之前拦截 可以做登陆判断、访问权限判断、记录日志。。。 方法return true是允许访问接口 return false是拒绝访问
     *
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
//        String requestUri = request.getRequestURI();
//        log.info("准备访问接口:" + requestUri);
        // token 校验
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(JSONUtil.toJsonStr(Result.ret(HttpServletResponse.SC_BAD_REQUEST, "token不能为空")));
            return false;
        }

        try {
            if (!JWTUtil.verify(token, key.getBytes())) {
                throw new ValidateException();
            }
            JWTValidator.of(token).validateDate();
        } catch (JWTException | JSONException | ValidateException e) {
            log.error("jwt verify error: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(JSONUtil.toJsonStr(Result.ret(HttpServletResponse.SC_UNAUTHORIZED, "token无效")));
            return false;
        }

        return true; // 放行所有接口
    }

    // 在controller方法执行完毕执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // log.info("controller执行完毕");
    }

    // 可以在这里捕捉异常
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // log.info("页面渲染完成，准备返回给客户端");
    }

}
