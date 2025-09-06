package com.parotia.persistor.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
