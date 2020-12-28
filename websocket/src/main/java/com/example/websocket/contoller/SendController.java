package com.example.websocket.contoller;


import com.example.websocket.websocket.WebSocketComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SendController {

    @Autowired
    private WebSocketComponent webSocketComponent;

    /**
     * 维护session 进行发送消息
     */
    @GetMapping("/send")
    public String send() {

        ConcurrentHashMap<String, Session> map = WebSocketComponent.getMap();
        if (map.entrySet().size() == 0) {
            return "index";
        }

        map.values().forEach(session -> {
            try {
                webSocketComponent.sendMessage(session, "开始发送消息给客户端.....您好你的缴费账单已出，请及时缴费！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        return "index";
    }
}
