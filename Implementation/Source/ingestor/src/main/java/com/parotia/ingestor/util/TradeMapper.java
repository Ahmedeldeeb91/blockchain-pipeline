package com.parotia.ingestor.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.parotia.common.dto.TradeMessage;
import com.parotia.ingestor.model.BinanceTradeDto;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    @Mapping(target = "eventId", source = "tradeId")
    @Mapping(target = "marketMaker", source = "isMarketMaker")
    @Mapping(target = "ignored", source = "isIgnored")
    TradeMessage toTradeMessageDto(BinanceTradeDto dto);
}