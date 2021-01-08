//package com.example.websocket.redison;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ResourceBundle;
//
//public class RedissonHolder {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonHolder.class);
//
//    private static final RedissonHolder HOLDER = new RedissonHolder();
//
//    private RedissonClient redisson;
//
//    private RedissonHolder() {
//
//        String location = ResourceBundle.getBundle("redisson").getString("redisson.json.location");
//        InputStream inputStream = RedissonHolder.class.getClassLoader().getResourceAsStream(location);
//        Config config = null;
//        try {
//            config = Config.fromJSON(inputStream);
//        } catch (IOException e) {
//            LOGGER.error("获取Redisson配置文件失败", e);
//        }
//        this.redisson = Redisson.create(config);
//    }
//
//
//    public static RedissonClient getClient() {
//        return HOLDER.redisson;
//    }
//}
