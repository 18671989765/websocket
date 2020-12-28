package com.example.websocket.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket")
public class WebSocketComponent {

    private Session session;

    public  static  ConcurrentHashMap<String,Session> map = new ConcurrentHashMap(100);

    private CopyOnWriteArraySet<WebSocketComponent> webSocketComponentSet = new CopyOnWriteArraySet<>();



    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        System.out.println(session.getId());
        map.put(session.getId(),session);
        webSocketComponentSet.add(this);
        System.out.println("连接成功");
        this.sendMessage(session,"发送给客户端的消息");
    }

    @OnClose
    public void onClose(){
        webSocketComponentSet.remove(this);
        map.remove(session.getId());
        System.out.println("连接关闭");
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("【接收消息】："+message);
    }

    public void sendMessage(Session session,String message) throws IOException {
        System.out.println("【发送消息】："+message);
        session.getBasicRemote().sendText(message);
    }

    public static ConcurrentHashMap getMap() {
        return map;
    }

    public static void setMap(ConcurrentHashMap map) {
        WebSocketComponent.map = map;
    }
}
