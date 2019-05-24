package com.lioncorp.example.store.proxy.dao.impl;

import com.lioncorp.example.store.proxy.dao.AbstractRedisClusterDao;
import com.lioncorp.example.store.proxy.dao.IRedisDao;
import com.lioncorp.example.store.proxy.domain.Item;
import com.lioncorp.example.store.proxy.util.RedisConstants;
import com.lioncorp.example.store.proxy.util.RedisResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Set;

@Component(IRedisDao.OTHER_REDIS)
public class OtherRedisDao extends AbstractRedisClusterDao implements IRedisDao {
    private static final Logger logger = LoggerFactory.getLogger(OtherRedisDao.class);

    @Autowired
    public OtherRedisDao(@Qualifier(RedisConstants.OTHER_REDIS) JedisCluster jedisCluster){
        super.jedisCluster = jedisCluster;
    }

    @Override
    public List<Item> get(String redisKey) {
        return RedisResponseUtil.transformProtoBufResponse(super.getByCluster(redisKey));
    }

    @Override
    public Set<String> sdiff(String redisKey) {
        return super.sdiffByCluster(redisKey);
    }
}
