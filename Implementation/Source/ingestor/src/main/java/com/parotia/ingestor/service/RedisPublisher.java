package com.parotia.ingestor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.parotia.ingestor.model.TradeMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${topic.trade.name}")
    private String tradeTopicName;

    public void publish(TradeMessage msg) {
        redisTemplate.convertAndSend(tradeTopicName, msg);
    }

}
