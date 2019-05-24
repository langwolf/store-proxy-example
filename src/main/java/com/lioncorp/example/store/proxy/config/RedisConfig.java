package com.lioncorp.example.store.proxy.config;

import com.google.common.collect.Sets;
import com.lioncorp.example.store.proxy.util.RedisConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

@Configuration
@ComponentScan(basePackages={"com.lioncorp.example.store.proxy"})
@PropertySource("classpath:conf/config.properties")
public class RedisConfig {

    @Bean(RedisConstants.COMMON_REDIS)
    public JedisCluster redisTemplateCommon(
            @Value("${redis.common.pool.max-idle}") int maxIdle,
            @Value("${redis.common.pool.min-idle}") int minIdle,
            @Value("${redis.common.pool.max-total}") int maxTotal,
            @Value("${redis.common.pool.max-wait}") int maxWait,
            @Value("${redis.common.timeout}") int timeout,
            @Value("${redis.common.cluster.nodes}") String nodes
    ) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWait);
        Set<HostAndPort> hostAndPorts = Sets.newHashSet();
        for(String node : nodes.split(",")) {
            if(StringUtils.isEmpty(node)) {
                continue;
            }
            node = node.trim();
            String ip = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            hostAndPorts.add(new HostAndPort(ip, port));
        }

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts, timeout, poolCofig);
        return jedisCluster;
    }

    @Bean(RedisConstants.OTHER_REDIS)
    public JedisCluster redisTemplateMf(
            @Value("${redis.other.pool.max-idle}") int maxIdle,
            @Value("${redis.other.pool.min-idle}") int minIdle,
            @Value("${redis.other.pool.max-total}") int maxTotal,
            @Value("${redis.other.pool.max-wait}") int maxWait,
            @Value("${redis.other.timeout}") int timeout,
            @Value("${redis.other.cluster.nodes}") String nodes
    ) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWait);
        Set<HostAndPort> hostAndPorts = Sets.newHashSet();
        for(String node : nodes.split(",")) {
            if(StringUtils.isEmpty(node)) {
                continue;
            }
            node = node.trim();
            String ip = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            hostAndPorts.add(new HostAndPort(ip, port));
        }

        JedisCluster jedisCluster = new JedisCluster(hostAndPorts, timeout, poolCofig);
        return jedisCluster;
    }

    @Bean(RedisConstants.PIKA)
    public JedisPool redisTemplateDspUser(
            @Value("${pika.pool.max-idle}") int maxIdle,
            @Value("${pika.pool.min-idle}") int minIdle,
            @Value("${pika.pool.max-total}") int maxTotal,
            @Value("${pika.pool.max-wait}") int maxWait,
            @Value("${pika.pool.test-while-idle}") boolean testWhileIdle,
            @Value("${pika.pool.test-on-borrow}") boolean testOnBorrow,
            @Value("${pika.host}") String host,
            @Value("${pika.port}") int port,
            @Value("${pika.timeout}") int timeout,
            @Value("${pika.password}") String password
    ) {

        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWait);
        poolCofig.setTestOnBorrow(testOnBorrow);
        poolCofig.setTestWhileIdle(testWhileIdle);

        JedisPool jedisPool = new JedisPool(poolCofig, host, port, timeout,password);
        return jedisPool;
    }
}
