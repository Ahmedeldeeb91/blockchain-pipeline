package com.parotia.ingestor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
@Order(1)
public class WebSocketConfig {

    @Bean
    public StandardWebSocketClient webSocketClient() {
        return new StandardWebSocketClient();
    }
}
