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
            logger.error("AbstractJedisUtil|get|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return null;
        } finally {
            close(jedis);
        }
        return val;
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        String val = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return null;
            }
            val = jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|hget|异常", e);
            if (jedis != null) {
                logger.info("AbstractJedisUtil|hget|error释放jedis");
                jedis.close();
            }
            return null;
        } finally {
            close(jedis);
        }
        return val;
    }

    public boolean set(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            jedis.set(key, val);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|set|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean incr(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
           jedis.incr(key);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|incr|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean set(byte[] key, byte[] val) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            jedis.set(key, val);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|set|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean hset(String key, String field, String val) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            jedis.hset(key, field, val);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|hset|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean zadd(String key, String field, double val,int expire) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            Pipeline pipeline = jedis.pipelined();
            Map<String,Response> responseMap = Maps.newHashMap();
            Response responseHset = pipeline.zadd(key,val,field);
            responseMap.put(key, responseHset);
            pipeline.expire(key, expire);
            pipeline.sync();
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|hset|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean hmset(String key, Map<String, String> val) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            jedis.hmset(key, val);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|hmset|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public boolean del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return false;
            }
            jedis.del(key);
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|del|异常", e);
            if (jedis != null) {
//                log.info("AbstractJedisUtil|del|error释放jedis");
                jedis.close();
            }
            return false;
        } finally {
            close(jedis);
        }
        return true;
    }

    public long setnx(String key, String val, int expire) {
        long setnx = 0;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (Objects.isNull(jedis)) {
                return 0;
            }
            Pipeline pipeline = jedis.pipelined();
            Response<Long> response = pipeline.setnx(key, val);
            pipeline.expire(key, expire);
            pipeline.sync();
            setnx = response.get();
        } catch (Exception e) {
            logger.error("AbstractJedisUtil|setnx|异常", e);
            if (jedis != null) {
                jedis.close();
            }
            return 0;
        } finally {
            close(jedis);
        }
        return setnx;
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
