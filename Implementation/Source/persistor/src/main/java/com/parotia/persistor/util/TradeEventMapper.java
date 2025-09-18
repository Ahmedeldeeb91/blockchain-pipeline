package com.parotia.persistor.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.parotia.common.dto.TradeMessage;
import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeRequest;
import com.parotia.persistor.model.TradeSymbol;

@Mapper(componentModel = "spring")
public interface TradeEventMapper {

    @Mapping(target = "symbol", source = "symbol", qualifiedByName = "mapSymbol")
    @Mapping(target = "isMarket", source = "marketMaker")
    @Mapping(target = "isIgnore", source = "ignored")
    TradeEvent toTradeEvent(TradeMessage msg);

    @Mapping(target = "symbol", source = "symbol", qualifiedByName = "mapSymbol")
    @Mapping(target = "isMarket", source = "marketMaker")
    @Mapping(target = "isIgnore", source = "ignored")
    TradeEvent toTradeEvent(TradeRequest request);

    @Named("mapSymbol")
    default TradeSymbol mapSymbol(String symbol) {
        try {
            System.out.println("Mapping symbol: " + symbol);
            return TradeSymbol.valueOf(symbol.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return TradeSymbol.UNKNOWN;
        }
    }
}