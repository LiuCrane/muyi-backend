package com.mysl.api.controller;

import com.mysl.api.common.GlobalConstant;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.config.security.JwtUserDetails;
import com.mysl.api.entity.JwtBlacklist;
import com.mysl.api.entity.enums.UserType;
import com.mysl.api.service.JwtBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Slf4j
public abstract class AbstractAuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtBlacklistService jwtBlacklistService;

    public String authenticate(String client, String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                ));
        final JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        UserType userType = userDetails.getUser().getType();
        if ((GlobalConstant.CLIENT_ADMIN.equals(client) && UserType.APP_USER.equals(userType))
                || (GlobalConstant.CLIENT_APP.equals(client) && UserType.BACKEND_USER.equals(userType))) {
            throw new ServiceException("登录失败");
        }
        return jwtTokenUtil.generateToken(userDetails);
    }

    public void signOut(String token) {
        jwtBlacklistService.save(JwtBlacklist.builder().token(token)
                .expirationTime(jwtTokenUtil.getExpirationDateFromToken(token)).build());
    }
}
