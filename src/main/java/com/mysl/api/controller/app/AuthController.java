package com.mysl.api.controller.app;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.LoginReqDTO;
import com.mysl.api.entity.dto.LoginResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ivan Su
 * @date 2022/7/27
 */
@Api(tags = "用户认证接口")
@RestController
@RequestMapping("/app/auth")
public class AuthController {

    @Value("${mysl.jwt.key}")
    private String key;

    @Value("${mysl.jwt.expiration}")
    private int expiration;

    /**
     * 登录
     * @param req
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResponseData<LoginResDTO> login(@Validated @RequestBody LoginReqDTO req) {
        // find user
        // ...
        String token = JWT.create()
                .setPayload("id", 1L)
                .setPayload("username", req.getUsername())
                .setExpiresAt(DateTime.now().offsetNew(DateField.SECOND, expiration))
                .setKey(key.getBytes()).sign();

        return ResponseData.ok(new LoginResDTO(token));
    }
}
