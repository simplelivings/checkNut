package com.example.checknut.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@CrossOrigin

class CheckNutControllerTest {


    @Autowired
    private WebSocketServer webSocketServer;

    @Test
    public void sendMessage() throws IOException {
        String cc = "nidaye";
        webSocketServer.sendInfo(cc);
    }
}
