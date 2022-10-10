package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.RegisterDTO;
import com.mysl.api.entity.dto.UserDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.common.OperateType;
import com.mysl.api.service.UserService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @EasyLog(module = "App-用户注册", tenant = "{getClientIP{0}}", type = OperateType.ADD, detail = "{{#dto.toString()}}",
            success = "", fail = "{{#_errMsg}}")
    @PostMapping("/register")
    public ResponseData register(@Validated @RequestBody RegisterDTO dto) {
        log.info("register dto: {}", dto);
        userService.register(dto);
        return ResponseData.ok();
    }

    @ApiOperation("查询用户信息")
    @EasyLog(module = "App-查询用户信息", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @Secured("ROLE_APP_USER")
    @GetMapping
        public ResponseData<UserDTO> getUserDetail() {
        UserDTO dto = JwtTokenUtil.getCurrentUser();
        return ResponseData.ok(dto);
    }

//    @ApiOperation("更新用户信息")
//    @Secured("ROLE_APP_USER")
//    @PutMapping
//    public ResponseData updateUserInfo(@RequestBody UserUpdateDTO dto) {
//        log.info("updateUserInfo dto: {}", dto);
//        return ResponseData.ok();
//    }

    @ApiOperation("修改密码")
    @EasyLog(module = "App-修改密码", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, success = "", fail = "{{#_errMsg}}")
    @Secured("ROLE_APP_USER")
    @PostMapping("/reset_password")
    public ResponseData updateUserPwd(@RequestBody UserPwdUpdateDTO dto) {
        userService.updatePassword(JwtTokenUtil.getCurrentUserId(), dto);
        return ResponseData.ok();
    }
}
