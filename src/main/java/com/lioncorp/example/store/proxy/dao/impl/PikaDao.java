package com.lioncorp.example.store.proxy.dao.impl;


import com.lioncorp.example.store.proxy.dao.AbstractJedisDao;
import com.lioncorp.example.store.proxy.dao.IRedisDao;
import com.lioncorp.example.store.proxy.domain.Item;
import com.lioncorp.example.store.proxy.util.RedisConstants;
import com.lioncorp.example.store.proxy.util.RedisResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

@Component(IRedisDao.PIKA)
public class PikaDao extends AbstractJedisDao implements IRedisDao {

    private static final Logger logger = LoggerFactory.getLogger(PikaDao.class);

    @Autowired
    public PikaDao(@Qualifier(RedisConstants.PIKA) JedisPool jedisPool){
        super.jedisPool = jedisPool;
    }

    @Override
    public JedisPool getJedisPool() {
        return super.jedisPool;
    }

    @Override
    public List<Item> get(String redisKey) {
        return RedisResponseUtil.transformProtoBufResponse(super.getByJedis(redisKey));
    }

    @Override
    public Set<String> sdiff(String redisKey) {
        return null;
    }
}
