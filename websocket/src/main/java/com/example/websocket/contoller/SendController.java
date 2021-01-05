package com.example.websocket.contoller;


import com.example.websocket.netty.Global;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SendController {

//    @Autowired
//    private WebSocketComponent webSocketComponent;
//
//    /**
//     * 维护session 进行发送消息
//     */
//    @GetMapping("/send")
//    public String send() {
//
//        ConcurrentHashMap<String, Session> map = WebSocketComponent.getMap();
//        if (map.entrySet().size() == 0) {
//            return "index";
//        }
//
//        map.values().forEach(session -> {
//            try {
//                webSocketComponent.sendMessage(session, "开始发送消息给客户端.....您好你的缴费账单已出，请及时缴费！");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        return "index";
//    }



    @GetMapping("/sendOne/{msg}")
    public void sendOne(@PathVariable String msg){
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString()  + "：" + msg);

        //这里进行发送数据 从netty中获取到channel 然后发送数据
            Global.group.writeAndFlush(tws);

    }

}
