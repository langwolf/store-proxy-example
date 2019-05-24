package com.netease.recsys2.test;

import com.lioncorp.example.store.proxy.dao.IRedisDao;
import com.lioncorp.example.test.SpringConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SpringConfig.class})
public class RedisTest {

    @Resource(name=IRedisDao.COMMON_REDIS)
    private IRedisDao redisDao;

    @Test
    public void redisTest(){
        System.out.println(redisDao.get("gsstest"));
    }

}
