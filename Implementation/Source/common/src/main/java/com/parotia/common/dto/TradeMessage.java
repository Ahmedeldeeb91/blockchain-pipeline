package com.parotia.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TradeMessage {
    long eventId;
    long eventTime;
    String symbol;
    BigDecimal price;
    BigDecimal quantity;
    long tradeTime;
    boolean isMarketMaker;
    boolean isIgnored;
}
