package com.mysl.api.controller.app;

import cn.hutool.core.util.NumberUtil;
import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.common.util.CoordUtil;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.controller.AbstractAuthController;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.AppLoginReqDTO;
import com.mysl.api.entity.dto.LoginReqDTO;
import com.mysl.api.entity.dto.LoginResDTO;
import com.mysl.api.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Ivan Su
 * @date 2022/7/27
 */
@Api(tags = "用户认证接口")
@RestController
@RequestMapping("/app/auth")
@Slf4j
public class AuthController extends AbstractAuthController {

    @Autowired
    StoreService storeService;

    @Value("${mysl.login.distance-limit}")
    Integer distanceLimit;

    /**
     * 登录
     * @param req
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResponseData<LoginResDTO> login(@Validated @RequestBody AppLoginReqDTO req) {
        final String token = super.authenticate(GlobalConstant.CLIENT_APP, req.getUsername(), req.getPassword());

        // 计算登录位置与注册时门店位置的距离
        Long storeId = JwtTokenUtil.getCurrentStoreId();
        Store store = storeService.getById(storeId);
        double distance = CoordUtil.getDistance(NumberUtil.toBigDecimal(store.getLng()).doubleValue(),
                NumberUtil.toBigDecimal(store.getLat()).doubleValue(),
                NumberUtil.toBigDecimal(req.getLng()).doubleValue(),
                NumberUtil.toBigDecimal(req.getLat()).doubleValue());
        log.info("login({}) distance: {}", req.getUsername(), distance);
        if (BigDecimal.valueOf(distance).compareTo(BigDecimal.valueOf(distanceLimit)) > 0) {
            throw new ServiceException("您的位置与注册地址偏差较大，请在指定位置登录！");
        }

        // 判断是否在工作时间(9:00-18:00)
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 9 || hour > 18) {
            throw new ServiceException("当前处于禁止登录时间，将退出应用。");
        }

        return ResponseData.ok(new LoginResDTO(token));
    }

    @ApiOperation("退出登录")
    @Secured("ROLE_APP_USER")
    @PostMapping("/logout")
    public ResponseData logout(@RequestHeader("Authorization") final String token) {
        super.signOut(token);
        return ResponseData.ok();
    }


}
