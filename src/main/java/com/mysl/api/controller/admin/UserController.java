package com.mysl.api.controller.admin;

import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.UserDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.service.UserService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Api(tags = "用户接口")
@Slf4j
@RestController("adminUserController")
@RequestMapping("/admin/user")
@Secured({"ROLE_ADMIN"})
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("查询用户信息")
    @EasyLog(module = "Admin-查询用户信息", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<UserDTO> getUserDetail() {
        UserDTO dto = JwtTokenUtil.getCurrentUser();
        return ResponseData.ok(dto);
    }

    @ApiOperation("修改密码")
    @EasyLog(module = "Admin-修改密码", type = OperateType.UPDATE, success = "", fail = "{{#_errMsg}}")
    @PostMapping("/reset_password")
    public ResponseData updateUserPwd(@RequestBody UserPwdUpdateDTO dto) {
        userService.updatePassword(JwtTokenUtil.getCurrentUserId(), dto);
        return ResponseData.ok();
    }
}
