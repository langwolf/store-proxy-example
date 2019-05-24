package com.lioncorp.example.store.proxy.dao;


import com.lioncorp.example.store.proxy.domain.Item;

import java.util.List;
import java.util.Set;


public interface IRedisDao {
    public static final String COMMON_REDIS = "common-redis";
    public static final String OTHER_REDIS = "other-redis";
    public static final String PIKA = "pika";

    public List<Item> get(String redisKey);
    public Set<String> sdiff(String redisKey);

}
