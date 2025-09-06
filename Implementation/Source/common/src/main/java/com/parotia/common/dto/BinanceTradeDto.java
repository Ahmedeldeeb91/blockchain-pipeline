package com.parotia.common.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)   
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BinanceTradeDto extends BinanceBaseEvent {

    @JsonProperty("s")
    String symbol; // Option trading symbol

    @JsonProperty("t")
    long tradeId;

    @JsonProperty("p")
    BigDecimal price;

    @JsonProperty("q")
    BigDecimal quantity;

    @JsonProperty("T")
    long tradeTime; // Trade completed time

    @JsonProperty("m")
    Boolean isMarketMaker;

    @JsonProperty("M")
    Boolean isIgnored;
}
