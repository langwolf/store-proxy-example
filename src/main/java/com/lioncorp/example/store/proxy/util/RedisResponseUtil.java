package com.lioncorp.example.store.proxy.util;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lioncorp.example.store.proxy.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import org.apache.commons.collections4.CollectionUtils;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RedisResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisResponseUtil.class);

    public static List<Item> transformSortedSetResponse(Set<Tuple> resp) {
        List<Item> result = Lists.newArrayList();
        //遍历set，组装数据
        Item recallItem = null;
        for (Tuple tuple : resp) {
            recallItem = new Item();
            String docId = tuple.getElement();
            double score = tuple.getScore();
            recallItem.setDocId(docId);
            recallItem.setScore(convertToIntScore(score));
            result.add(recallItem);
        }
        return result;
    }

    public static List<Item> transformProtoBufResponse(byte[] resp) {
        if (Objects.isNull(resp)) {
            return Collections.emptyList();
        }
        List<Item> resultList = Lists.newArrayList();
        try {
            com.netease.recsys2.recall.proto.RevertedDocListOuterClass.RevertedDocList revertedDocList = com.netease.recsys2.recall.proto.RevertedDocListOuterClass.RevertedDocList.parseFrom(resp);
            if (revertedDocList == null) {
                return null;
            }
            List<com.netease.recsys2.recall.proto.RevertedDocListOuterClass.RevertedDocList.DocItem> docItemList = revertedDocList.getDocItemList();
            if (CollectionUtils.isEmpty(docItemList)) {
                return null;
            }
            //docItemList 多条数据
            for (int i = 0; i < docItemList.size(); i++) {
                com.netease.recsys2.recall.proto.RevertedDocListOuterClass.RevertedDocList.DocItem docItem = docItemList.get(i);
                addResultList(docItem, resultList, i);
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error("RedisResponseUtil|transformProtoBufResponse|反序列化异常", e);
            return Collections.emptyList();
        }
        return resultList;
    }

    private static void addResultList(com.netease.recsys2.recall.proto.RevertedDocListOuterClass.RevertedDocList.DocItem docItem, List<Item> resultList, Integer index) {
        Item item = new Item();
        String docId = docItem.getDocId();
        String scoreStr = Float.toString(docItem.getScore());
        BigDecimal score = new BigDecimal(scoreStr).multiply(new BigDecimal(10000));
        int scoreInt = score.intValue();
        item.setDocId(docId);
        item.setScore(scoreInt);
        item.setRank(index + 1);
        resultList.add(item);
    }

    private static Integer convertToIntScore(double score) {
        BigDecimal scoreB = new BigDecimal(score);
        return scoreB.multiply(new BigDecimal(10000)).intValue();
    }
}
