package com.parotia.ingestor.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.parotia.ingestor.model.BinanceTradeDto;
import com.parotia.ingestor.model.TradeMessage;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    @Mapping(source = "tradeId", target = "id")
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "price", target = "price")
    TradeMessage toTradeMessageDto(BinanceTradeDto dto);
}