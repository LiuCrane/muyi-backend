package com.mysl.api.controller.app;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.RegisterDTO;
import com.mysl.api.entity.dto.UserDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.entity.dto.UserUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Api(tags = "用户接口")
@Slf4j
@RestController
@RequestMapping("/app/user")
public class UserController {

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseData register(@RequestBody RegisterDTO dto) {
        log.info("register dto: {}", dto);
        return ResponseData.ok();
    }

    /**
     * 获取用户详情
     *
     * @param token
     * @return
     */
    @ApiOperation("查询用户信息")
    @GetMapping
    public ResponseData<UserDTO> getUserDetail(@ApiParam("用户token") @RequestHeader("Authorization") final String token) {
        JWT jwt = JWTUtil.parseToken(token);
        UserDTO dto = UserDTO.builder()
                .id(Long.parseLong(jwt.getPayload("id").toString()))
                .username((String) jwt.getPayload("username"))
                .realName("张三")
                .phone((String) jwt.getPayload("username"))
                .nickname("Gray")
                .avatarUrl("https://mysl.tianyuekeji.ltd/upload/img/1403241300318228481.png")
                .build();
        return ResponseData.ok(dto);
    }

    @ApiOperation("更新用户信息")
    @PutMapping
    public ResponseData updateUserInfo(@RequestBody UserUpdateDTO dto) {
        log.info("updateUserInfo dto: {}", dto);
        return ResponseData.ok();
    }

    @ApiOperation("修改密码")
    @PatchMapping
    public ResponseData updateUserPwd(@RequestBody UserPwdUpdateDTO dto) {
        return ResponseData.ok();
    }
}
