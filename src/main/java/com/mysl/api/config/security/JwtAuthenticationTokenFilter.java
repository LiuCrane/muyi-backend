package com.mysl.api.config.security;

import com.mysl.api.service.JwtBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AuthorizationServiceException;
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

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

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
            username = jwtTokenUtil.getUsernameFromToken(authToken);
        }

        log.debug("request uri: {}", request.getRequestURI());
        log.debug("authToken : {},username : {}", authToken, username);

        if (username != null) {
            if (blacklistService.isExist(authToken)) {
                throw new AuthorizationServiceException("the token is in blacklist");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    log.debug("authToken : {},username : {}", authToken, username);

                    log.debug("该 " + username + "用户已认证, 设置安全上下文");

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
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
}
