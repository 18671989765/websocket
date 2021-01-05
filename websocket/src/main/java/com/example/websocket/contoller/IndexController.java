package com.example.websocket.contoller;

import com.example.websocket.netty.Global;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {



    @GetMapping("/index/{msg}")
    public String index(@PathVariable String msg)
    {
        return "index";
    }




}
