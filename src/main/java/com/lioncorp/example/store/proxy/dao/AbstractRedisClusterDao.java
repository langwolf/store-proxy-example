package com.lioncorp.example.store.proxy.dao;


import com.lioncorp.example.store.proxy.util.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractRedisClusterDao {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisClusterDao.class);

    public  JedisCluster jedisCluster;

    public byte[] getByCluster(String redisKey){
        byte[] resp = null;
        try {
            resp = jedisCluster.get(redisKey.getBytes(RedisConstants.CHARSET));
        } catch (Exception e) {
            logger.error("recall get error", e);
            return null;
        }
        if (Objects.isNull(resp)) {
            return null;
        }
        return resp;
    }

    public Set<String> sdiffByCluster(String redisKey){
        Set<String> resp = null;
        try {
            resp = jedisCluster.sdiff(redisKey);
        } catch (Exception e) {
            logger.error("recall getByMf error", e);
            return Collections.emptySet();
        }
        if (Objects.isNull(resp)) {
            return Collections.emptySet();
        }
        return resp;
    }
}
