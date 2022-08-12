package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.RegisterDTO;
import com.mysl.api.entity.dto.UserDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.entity.dto.UserUpdateDTO;
import com.mysl.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseData register(@Validated @RequestBody RegisterDTO dto) {
        log.info("register dto: {}", dto);
        userService.register(dto);
        return ResponseData.ok();
    }

    /**
     * 获取用户详情
     *
     * @param token
     * @return
     */
    @ApiOperation("查询用户信息")
    @Secured("ROLE_APP_USER")
    @GetMapping
    public ResponseData<UserDTO> getUserDetail(@ApiParam("用户token") @RequestHeader("Authorization") final String token) {
        UserDTO dto = JwtTokenUtil.getCurrentUser();
        log.info("dto: {}", dto);
        return ResponseData.ok(dto);
    }

    @ApiOperation("更新用户信息")
    @Secured("ROLE_APP_USER")
    @PutMapping
    public ResponseData updateUserInfo(@RequestBody UserUpdateDTO dto) {
        log.info("updateUserInfo dto: {}", dto);
        return ResponseData.ok();
    }

    @ApiOperation("修改密码")
    @Secured("ROLE_APP_USER")
    @PatchMapping
    public ResponseData updateUserPwd(@RequestBody UserPwdUpdateDTO dto) {
        return ResponseData.ok();
    }
}
