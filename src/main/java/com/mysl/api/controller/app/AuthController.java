package com.mysl.api.controller.app;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.jwt.JWT;
import com.mysl.api.common.lang.Result;
import com.mysl.api.entity.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ivan Su
 * @date 2022/7/27
 */
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
    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginDTO req) {
        // find user
        // ...
        String token = JWT.create()
                .setPayload("id", 1L)
                .setPayload("username", req.getUsername())
                .setExpiresAt(DateTime.now().offsetNew(DateField.SECOND, expiration))
                .setKey(key.getBytes()).sign();

        return Result.ok(MapUtil.of("token", token));
    }
}
