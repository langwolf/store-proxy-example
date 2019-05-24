package com.lioncorp.example.store.proxy.util;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lioncorp.example.store.proxy.domain.Item;
import com.lioncorp.example.store.proxy.proto.RevertedDocListOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections4.CollectionUtils;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RedisResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisResponseUtil.class);

    public static List<Item> transformProtoBufResponse(byte[] resp) {
        if (Objects.isNull(resp)) {
            return Collections.emptyList();
        }
        List<Item> resultList = Lists.newArrayList();
        try {
            RevertedDocListOuterClass.RevertedDocList revertedDocList = RevertedDocListOuterClass.RevertedDocList.parseFrom(resp);
            if (revertedDocList == null) {
                return Collections.emptyList();
            }
            List<RevertedDocListOuterClass.RevertedDocList.DocItem> docItemList = revertedDocList.getDocItemList();
            if (CollectionUtils.isEmpty(docItemList)) {
                return Collections.emptyList();
            }
            for (int i = 0; i < docItemList.size(); i++) {
                RevertedDocListOuterClass.RevertedDocList.DocItem docItem = docItemList.get(i);
                addResultList(docItem, resultList, i);
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error("RedisResponseUtil|transformProtoBufResponse|Exception", e);
            return Collections.emptyList();
        }
        return resultList;
    }

    private static void addResultList(RevertedDocListOuterClass.RevertedDocList.DocItem docItem, List<Item> resultList, Integer index) {
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
}
