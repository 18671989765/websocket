package com.example.websocket.websocketclient;

import org.java_websocket.WebSocket;

import java.net.URI;

public class RannableTest extends Thread {

    // 根据实际websocket地址更改
    private static String url = "ws://localhost:8081/websocket";

    @Override
    public void run() {
        try {
            WsClient myClient = new WsClient(new URI(url));
            myClient.connect();
            // 判断是否连接成功，未成功后面发送消息时会报错
            while (!myClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("调用成功.....");
            }
            myClient.send("MyClient");
            System.out.println("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
