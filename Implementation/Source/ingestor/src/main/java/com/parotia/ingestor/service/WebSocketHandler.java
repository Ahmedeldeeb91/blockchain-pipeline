package com.parotia.ingestor.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parotia.ingestor.model.BinanceTradeDto;
import com.parotia.ingestor.model.TradeMessage;
import com.parotia.ingestor.util.TradeMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    RedisPublisher redisPublisher;
    TradeMapper tradeMapper;
    StandardWebSocketClient client;
    @NonFinal
    @Value("${webSocket.uri}")
    String WS_URL;
    ObjectMapper objectMapper;
    @NonFinal
    AtomicInteger attemptCount = new AtomicInteger(0);
    @NonFinal
    volatile WebSocketSession session;
    @NonFinal
    ScheduledExecutorService scheduler = java.util.concurrent.Executors
            .newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
        log.info("afterConnectionEstablished: {}", session.getId());
    }

    @PostConstruct
    private void connect() {
        log.info("Connecting to WebSocket: {}", WS_URL);
        client.execute(this, WS_URL)
                .whenComplete((wsSession, ex) -> {
                    if (ex != null) {
                        long delay = calcBackoffSeconds(attemptCount.incrementAndGet());
                        log.warn("Connection failed (attempt={}): retry in {}s",
                                attemptCount.get(), delay, ex);
                        scheduler.schedule(this::connect, delay, TimeUnit.SECONDS);
                    } else {
                        attemptCount.set(0);
                        this.session = wsSession;
                        log.info("Connected to WebSocket session.id: {}", wsSession.getId());
                    }
                });
    }

    private long calcBackoffSeconds(int attempts) {
        // 1,2,4,8,16,32, cap at 60s
        int exponent = Math.min(attempts, 6);
        long seconds = (long) Math.pow(2, exponent - 1); // attempt 1 => 1s
        return Math.max(1, Math.min(60, seconds));
    }

    private void scheduleReconnect() {
        int a = attemptCount.incrementAndGet();
        long delay = calcBackoffSeconds(a);
        log.info("Scheduling reconnect in {}s (attempt={})", delay, a);
        scheduler.schedule(this::connect, delay, TimeUnit.SECONDS);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            log.info("Received raw message: {}", payload);
            BinanceTradeDto binance = objectMapper.readValue(payload, BinanceTradeDto.class);
            log.info("Received trade: {}", binance);
            TradeMessage tradeMessage = tradeMapper.toTradeMessageDto(binance);
            redisPublisher.publish(tradeMessage);
            log.info("Published trade message: {}", tradeMessage);
        } catch (Exception e) {
            log.error("Failed to parse/convert trade message: {}", payload, e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.warn("Connection closed: {}. Scheduling reconnect.", status);
        this.session = null;
        scheduleReconnect();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error on session {}, closing and reconnecting", session != null ? session.getId() : "n/a",
                exception);
        try {
            if (session != null && session.isOpen())
                session.close();
        } catch (Exception ex) {
            log.warn("Error while closing session after transport error", ex);
        }
        this.session = null;
        scheduleReconnect();
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down WebSocket");
        try {
            if (session != null && session.isOpen())
                session.close();
        } catch (Exception ignored) {
        }
        scheduler.shutdownNow();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                log.warn("Reconnect scheduler did not terminate cleanly");
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}