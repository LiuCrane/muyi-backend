package com.mysl.api.config;

import com.alibaba.fastjson.JSON;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ivan Su
 * @date 2022/9/4
 */
@Slf4j
@ServerEndpoint(value = "/ws/admin/get_application_count", configurator = WebSocketConfig.class)
@Component
public class WebSocketServer {

    private static JwtTokenUtil jwtTokenUtil;
    private static UserDetailsService userDetailsService;
    private static ApplicationService applicationService;

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        WebSocketServer.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        WebSocketServer.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setApplicationService(ApplicationService applicationService) {
        WebSocketServer.applicationService = applicationService;
    }

    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet
            = new CopyOnWriteArraySet<>();
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        Map<String, List<String>> paramMap = session.getRequestParameterMap();
        List<String> tokens = paramMap.get("token");
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        try {
            if (CollectionUtils.isEmpty(tokens)) {
                sendMessage(JSON.toJSONString(ResponseData.generator(400101)));
                onClose();
            } else {
                if (!validToken(tokens.get(0))) {
                    sendMessage(JSON.toJSONString(ResponseData.generator(400101)));
                    onClose();
                } else {
                    int count = applicationService.countApplications();
                    sendMessage(JSON.toJSONString(ResponseData.ok(count)));
                }
            }
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    //实现服务器主动推送
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static void send(String message) {
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error("ws send:", e);
            }
        }
    }

    private boolean validToken(String token) {
        try {
            String username = null;
            if (StringUtils.isNotBlank(token)) {
                username = jwtTokenUtil.getUsernameFromToken(token);
            }
            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("getAuthorities: {}", userDetails.getAuthorities());
                if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    return false;
                }
                if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(token, userDetails))) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("WebSocketServer validToken error: ", e);
        }
        return false;
    }
}
