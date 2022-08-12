package com.mysl.api.controller;

import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.JwtBlacklist;
import com.mysl.api.service.JwtBlacklistService;
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
public abstract class AbstractAuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtBlacklistService jwtBlacklistService;

    public String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenUtil.generateToken(userDetails);
    }

    public void signOut(String token) {
        jwtBlacklistService.save(JwtBlacklist.builder().token(token)
                .expirationTime(jwtTokenUtil.getExpirationDateFromToken(token)).build());
    }
}
