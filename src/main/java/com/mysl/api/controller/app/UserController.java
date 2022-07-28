package com.mysl.api.controller.app;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.mysl.api.common.lang.Result;
import com.mysl.api.entity.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Slf4j
@RestController
@RequestMapping("/app")
public class UserController {

    /**
     * 获取用户详情
     *
     * @param token
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity getUserDetail(@RequestHeader("Authorization") final String token) {
        JWT jwt = JWTUtil.parseToken(token);
        UserDTO dto = UserDTO.builder()
                .id(Long.parseLong(jwt.getPayload("id").toString()))
                .username((String) jwt.getPayload("username"))
                .realName("张三")
                .phone((String) jwt.getPayload("username"))
                .nickname("Gray")
                .avatarUrl("https://mysl.tianyuekeji.ltd/upload/img/1403241300318228481.png")
                .build();
        return Result.ok(dto);
    }
}
