package com.parotia.persistor.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class TradeRequest {
    String symbol;
    Long eventId; // corresponds to Binance tradeId
    Long eventTime;
    BigDecimal quantity;
    BigDecimal price;
    long tradeTime;
    boolean isMarketMaker;
    boolean isIgnored;
}
