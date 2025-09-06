package com.parotia.persistor.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeMessage;
import com.parotia.persistor.repo.TradeEventRepo;
import com.parotia.persistor.util.TradeEventMapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RedisMessageSubscriber implements MessageListener {
    ObjectMapper objectMapper;
    TradeEventRepo tradeRepo;
    TradeEventMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        log.debug("Received message: {}", msg);

        try {
            TradeMessage trade = objectMapper.readValue(msg, TradeMessage.class);
            TradeEvent tradeEvent = mapper.toTradeEvent(trade);
            tradeRepo.save(tradeEvent);
            log.info("Trade persisted: {}", tradeEvent);
        } catch (Exception e) {
            log.error("Failed to persist trade message", e);
        }
    }
}
