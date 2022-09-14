package com.mysl.api.config.security;

import com.alibaba.fastjson.JSON;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.service.JwtBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;
    private final JwtBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String authToken = this.extractAuthTokenFromRequest(request, this.tokenHeader);
        String username = null;
        if (StringUtils.isNotBlank(authToken)) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (Exception e) {
                setErrorResponse(400101, response, e);
                return;
            }
        }

        log.debug("request uri: {}", request.getRequestURI());
        log.debug("authToken : {},username : {}", authToken, username);

        if (username != null) {
            if (blacklistService.isExist(authToken)) {
                setErrorResponse(400101, response, null);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(authToken, userDetails))) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    log.debug("authToken : {},username : {}", authToken, username);

                    log.debug("该 " + username + "用户已认证, 设置安全上下文");

                    ThreadContext.put(USER_ID, String.valueOf(((JwtUserDetails) userDetails).getUser().getId()));
                    ThreadContext.put(USERNAME, username);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
        ThreadContext.clearAll();
    }

    private String extractAuthTokenFromRequest(HttpServletRequest httpRequest, String tokenHeader) {
        /* Get token from header */
        String authToken = httpRequest.getHeader(tokenHeader);
        /* 如果请求头没有找到,那么从请求参数中获取 */
        if (StringUtils.isEmpty(authToken)) {
            authToken = httpRequest.getParameter(tokenHeader);
        }
        return authToken;
    }

    private void setErrorResponse(int bizCode, HttpServletResponse response, Throwable ex){
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(JSON.toJSONString(ResponseData.generator(bizCode)));
        } catch (IOException e) {
            log.error("setErrorResponse error: ", e);
        }
    }
}
