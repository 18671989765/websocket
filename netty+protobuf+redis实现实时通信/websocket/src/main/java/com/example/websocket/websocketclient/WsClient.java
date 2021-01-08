package com.example.websocket.websocketclient;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;


public class WsClient extends WebSocketClient {

private AtomicInteger atomicInteger = new AtomicInteger(0);

    public WsClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        int i = atomicInteger.addAndGet(1);
        System.out.println("握手成功"+i);
    }

    @Override
    public void onMessage(String s) {
        System.out.println("接受到的消息"+s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("关闭链接");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("发生异常");
    }
}
