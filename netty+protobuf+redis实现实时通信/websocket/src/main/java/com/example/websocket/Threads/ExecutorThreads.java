package com.example.websocket.Threads;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorThreads {


    @PostConstruct
    public static ExecutorService executorService() {
        return Executors.newFixedThreadPool(1000);
    }
}
