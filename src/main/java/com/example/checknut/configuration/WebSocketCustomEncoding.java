package com.example.checknut.configuration;

import com.alibaba.fastjson.JSON;
import com.example.checknut.entity.BasicInfo;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * TODO
 * WebSocet发送对象的配置类
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-15 14:25
 */

public class WebSocketCustomEncoding implements Encoder.Text<Object>{
    @Override
    public String encode(Object object) throws EncodeException {
        assert object != null;
        return JSON.toJSONString(object);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
