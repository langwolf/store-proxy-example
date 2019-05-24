package com.lioncorp.example.store.proxy.dao;


import com.google.common.collect.Maps;
import com.lioncorp.example.store.proxy.util.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractJedisDao {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJedisDao.class);
    public JedisPool jedisPool;

    public byte[] getByJedis(String key) {
        Jedis jedis = null;
        byte[] val = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return null;
            }
            val = jedis.get(key.getBytes(RedisConstants.CHARSET));
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|get|exception", e);
            if (jedis != null) {
                jedis.close();
            }
            return null;
        } finally {
            close(jedis);
        }
        return val;
    }

    public Jedis getJedis() {
        Jedis jedis = null;
        JedisPool jedisPool = null;
        try {
            jedisPool = getJedisPool();
            if (Objects.isNull(jedisPool)) {
                logger.info("AbstractJedisUtil|getJedis|jedisPool is null");
                return null;
            }
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            if (Objects.nonNull(jedis)) {
                logger.error("AbstractJedisUtil|getJedis error,jedis not null", e);
                jedis.close();
                if (Objects.isNull(jedis)) {
                    logger.info("getJedis|exception close jedis connection,jedis is null");
                } else {
                    logger.info("getJedis|exception close jedis connection,jedis not null");
                    jedis = null;
                }
            } else {
                logger.error("AbstractJedisUtil|getJedis error,jedis is null", e);
            }
        }
        return jedis;
    }

    private void close(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|close error", e);
        }
    }

    public abstract JedisPool getJedisPool();
}
