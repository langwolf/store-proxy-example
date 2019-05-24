package com.netease.recsys2.recall.jedis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Deprecated
public class JedisClient implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(JedisClient.class);

    private ApplicationContext applicationContext;

    private static Map<String,JedisCluster> jedisClusterMap = Maps.newHashMap();

    private static String jedisInstranceNames;

    @Value("${redis.recall.instrance-names}")
    public void setInstranceName(String jedisInstanceNames) {
        JedisClient.jedisInstranceNames = jedisInstanceNames;
    }

    private static class JedisHolder {
        private static final  Map<String,JedisCluster> INSTRANCES = jedisClusterMap;
    }

    @PostConstruct
    public void init() throws Exception{
        logger.info("InitJedisCluster start");
        //
        List<String> instranceNames = getInstranceNames(jedisInstranceNames);
        if(CollectionUtils.isEmpty(instranceNames)) {
            logger.info("jedisInstranceNames is null");
            throw new Exception("jedisInstranceNames is null");
        }
        JedisCluster bean = null;
        for(String instranceName :instranceNames) {
            bean = (JedisCluster)applicationContext.getBean(instranceName);
            jedisClusterMap.put(instranceName, bean);
        }
        logger.info("InitJedisCluster end");
    }

    public static JedisCluster getJedisCluster(String instranceName) {
        return JedisHolder.INSTRANCES.get(instranceName);
    }

    private static List<String> getInstranceNames(String jedisInstranceNames) {
        if (StringUtils.isBlank(jedisInstranceNames)) {
            return Collections.emptyList();
        }
        List result = Lists.newArrayList();
        String[] jedisInstranceNamesStr = jedisInstranceNames.split(",");
        for (String jedisInstanceName : jedisInstranceNamesStr) {
            result.add(jedisInstanceName);
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
