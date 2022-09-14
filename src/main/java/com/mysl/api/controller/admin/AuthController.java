package com.mysl.api.controller.admin;

import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.controller.AbstractAuthController;
import com.mysl.api.entity.dto.LoginReqDTO;
import com.mysl.api.entity.dto.LoginResDTO;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Ivan Su
 * @date 2022/7/27
 */
@Api(tags = "用户认证接口")
@RestController("adminAuthController")
@RequestMapping("/admin/auth")
public class AuthController extends AbstractAuthController {

    /**
     * 登录
     * @param req
     * @return
     */
    @ApiOperation(value = "登录")
    @EasyLog(module = "Admin-登录", success = "", fail = "{{#_errMsg}}", detail = "req content: {{#req.toString()}}")
    @PostMapping("/login")
    public ResponseData<LoginResDTO> login(@Validated @RequestBody LoginReqDTO req) {
        final String token = super.authenticate(GlobalConstant.CLIENT_ADMIN, req.getUsername(), req.getPassword());
        return ResponseData.ok(new LoginResDTO(token));
    }

    @ApiOperation("退出登录")
    @EasyLog(module = "Admin-退出登录", success = "", fail = "{{#_errMsg}}", detail = "{{#token}}")
    @Secured("ROLE_ADMIN")
    @PostMapping("/logout")
    public ResponseData logout(@RequestHeader("Authorization") final String token) {
        super.signOut(token);
        return ResponseData.ok();
    }
}
