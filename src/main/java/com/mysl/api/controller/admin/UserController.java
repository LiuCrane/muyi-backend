package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.UserDTO;
import com.mysl.api.entity.dto.UserFullDTO;
import com.mysl.api.entity.dto.UserPwdUpdateDTO;
import com.mysl.api.entity.dto.UserSimpleDTO;
import com.mysl.api.service.UserService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Api(tags = "用户接口")
@Slf4j
@RestController("adminUserController")
@RequestMapping("/admin")
@Secured({"ROLE_ADMIN"})
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("查询用户列表")
    @EasyLog(module = "Admin-查询用户列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/users")
    public ResponseData<PageInfo<UserFullDTO>> list(@ApiParam("页数，默认 1")
                                                    @RequestParam(name = "page_num", required = false, defaultValue = "1")
                                                    Integer pageNum,
                                                    @ApiParam("每页记录，默认 20")
                                                    @RequestParam(name = "page_size", required = false, defaultValue = "20")
                                                    Integer pageSize,
                                                    @ApiParam("手机号") @RequestParam(required = false) String phone,
                                                    @ApiParam("姓名") @RequestParam(required = false) String name) {
        return ResponseData.ok(userService.getUsers(pageNum, pageSize, phone, name));
    }

    @ApiOperation("查询全部用户(前端下拉列表可用)")
    @EasyLog(module = "Admin-查询用户列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/simple_users")
    public ResponseData<List<UserSimpleDTO>> getSimpleList() {
        return ResponseData.ok(userService.getSimpleUsers());
    }

    @ApiOperation("查询个人信息")
    @EasyLog(module = "Admin-查询个人信息", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/me")
    public ResponseData<UserDTO> getUserDetail() {
        UserDTO dto = JwtTokenUtil.getCurrentUser();
        return ResponseData.ok(dto);
    }

    @ApiOperation("修改个人密码")
    @EasyLog(module = "Admin-修改个人密码", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, success = "", fail = "{{#_errMsg}}")
    @PostMapping("/me/reset_password")
    public ResponseData updateUserPwd(@RequestBody UserPwdUpdateDTO dto) {
        userService.updatePassword(JwtTokenUtil.getCurrentUserId(), dto);
        return ResponseData.ok();
    }
}
