//package com.example.websocket.redison;
//
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//
//@Configuration
//@ConditionalOnClass(Config.class)
//@EnableConfigurationProperties(RedissonProperties.class)
//public class RedissonAutoConfiguration {
//
//    @Autowired
//    private RedissonProperties redissonProperties;
//
//    /**
//     * 单机模式自动装配
//     * @return
//     */
//    @Bean
//    @ConditionalOnProperty(name="redisson.address")
//    public RedissonClient redissonSingle() {
//        Config config = new Config();
//
//        SingleServerConfig serverConfig = config.useSingleServer()
//                .setAddress(redissonProperties.getAddress())
//                .setTimeout(redissonProperties.getTimeout())
//
//                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
//
//        if(!StringUtils.isEmpty(redissonProperties.getPassword())) {
//            serverConfig.setPassword(redissonProperties.getPassword());
//
//        }
//
//        return Redisson.create(config);
//    }
//
//    /**
//     * 装配locker类，并将实例注入到RedissLockUtil中
//     * @return
//     */
//    @Bean
//    DistributedLocker distributedLocker(RedissonClient redissonClient) {
//        DistributedLocker locker = new RedissonDistributedLocker();
//        ((RedissonDistributedLocker) locker).setRedissonClient(redissonClient);
//        RedissonLockUtil.setLocker(locker);
//        return locker;
//    }
//
//}
