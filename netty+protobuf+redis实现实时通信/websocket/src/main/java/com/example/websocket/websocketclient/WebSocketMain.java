package com.example.websocket.websocketclient;

import com.example.websocket.Threads.ExecutorThreads;

import java.util.concurrent.ExecutorService;

public class WebSocketMain {


    public static void main(String[] args) {

        ExecutorService executorService = ExecutorThreads.executorService();

        for (int i = 0; i < 100; i++) {

            new RannableTest().start();
        }


    }


}
