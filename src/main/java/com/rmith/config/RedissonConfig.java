package com.rmith.config;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 
 */
@Configuration
public class RedissonConfig {

    @Value("${custom.config.redis.host}")
    private String host;

    @Value("${custom.config.redis.port}")
    private int port;

    @Value("${custom.config.redis.poolsize}")
    private int poolsize;

    @Value("${custom.config.redis.subpoolsize}")
    private int subpoolsize;

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws InterruptedException {
        System.out.println("\n################## Init Redission Config ##################");
        Config config = new Config();
        config.useSingleServer()
                /* address and port of redis server */
                .setAddress(
                        new StringBuilder("redis://").append(host).append(":").append(port).toString())
                /* max alive time of IDLE connection in ms */
                .setIdleConnectionTimeout(10000)
                /* ping to check alive connection in ms */
                .setPingTimeout(2000)
                /* timeout when connect to server in ms, if timeout, server will attempt to retry by followin config */
                .setConnectTimeout(10000)
                /* timeout request in ms */
                .setTimeout(3000)
                /* max retry attempt if client cannot connect to server */
                .setRetryAttempts(3)
                /* time to wait before sending another connect request */
                .setRetryInterval(1500)
                .setSubscriptionsPerConnection(5)
                .setSubscriptionConnectionMinimumIdleSize(1)
                .setSubscriptionConnectionPoolSize(subpoolsize)
                /*
                * Minimum IDLE connections keep in pool, 10 is enough.
                * Don't set it too high because it will waste your resources.
                * Don't set it too low since it will take time to init new connection.
                 */
                .setConnectionMinimumIdleSize(10)
                /* Time check dns of redisson monitoring */
                .setDnsMonitoringInterval(5000)
                /* max connection in pool */
                .setConnectionPoolSize(poolsize)
                /* selected database in server */
                .setDatabase(1);
        RedissonClient redissonClient = Redisson.create(config);
        System.out.println(
                "################## Init Redission Config Successfully ##################");
        return redissonClient;
    }

}
