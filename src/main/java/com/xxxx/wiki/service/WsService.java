package com.xxxx.wiki.service;

import com.xxxx.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by stfu
 * 2021-07-24 17:22
 */
@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 异步化推送消息
     * @param message
     */
    @Async
    public void sendInfo(String message, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(message);
    }

}
