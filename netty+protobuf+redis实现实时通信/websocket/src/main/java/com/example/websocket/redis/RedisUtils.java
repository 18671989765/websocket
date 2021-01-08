package com.example.websocket.redis;

import com.example.websocket.config.ApplicationContextHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisUtils {

    @PostConstruct
    public static RedisTemplate redisTemplate() {

        return (RedisTemplate) ApplicationContextHelper.getBean("redisTemplate");
    }
}
