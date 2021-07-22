package com.example.checknut.controller;

import com.example.checknut.configuration.WebSocketCustomEncoding;
import com.example.checknut.entity.BasicInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-15 11:44
 */
@Component("wb")
//@ServerEndpoint("/T215300630/push")
@ServerEndpoint(value = "/push", encoders = WebSocketCustomEncoding.class)
@Slf4j
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) throws EncodeException, IOException {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid=sid;
        System.out.println("WebSocketSession ==" + this.session);
        System.out.println("WebSocket ==" + this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //log.info("收到来自窗口"+sid+"的信息:"+message);
        if("heart".equals(message)){
            try {
                sendMessage("heartOk");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        System.out.println("session ==" + this.session);
        System.out.println("message ==" + message);
        System.out.println("Remote ==" + this.session.getBasicRemote());
        this.session.getBasicRemote().sendText(message);
    }

    /**
     *
     * 发送对象至前端
     *
     */
    public void sendObj(Object object) throws IOException {
        try {
            this.session.getBasicRemote().sendObject(object);
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送对象至前端
     * */
    public static void sendObjectTo(Object object) throws IOException {

        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendObj(object);
                log.info("推送消息到窗口"+item.sid+"，推送内容:"+object);
            } catch (IOException e) {
                continue;
            }
        }
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
//                if(sid==null) {

                item.sendMessage(message);
                log.info("推送消息到窗口"+item.sid+"，推送内容:"+message);
//                }else if(item.sid.equals(sid)){
//                    item.sendMessage(message);
//                }
            } catch (IOException e) {
                continue;
            }
        }
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

}
