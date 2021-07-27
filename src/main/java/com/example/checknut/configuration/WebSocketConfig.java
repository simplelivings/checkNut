package com.example.checknut.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * TODO
 * WebSocket配置类;
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-15 11:38
 * 向前端推送消息，WebSocket配置类
 *
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public  ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
