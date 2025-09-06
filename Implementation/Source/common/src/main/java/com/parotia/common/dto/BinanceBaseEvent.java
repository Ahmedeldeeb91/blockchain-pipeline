package com.parotia.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BinanceBaseEvent {
    @JsonProperty("e")
    String eventType;
    @JsonProperty("E")
    long eventTime;
}
