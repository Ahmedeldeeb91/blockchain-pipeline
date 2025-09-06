package com.parotia.persistor.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeMessage;
import com.parotia.persistor.model.TradeSymbol;

@Mapper(componentModel = "spring")
public interface TradeEventMapper {

    @Mapping(target = "symbol", source = "symbol", qualifiedByName = "mapSymbol")
    TradeEvent toTradeEvent(TradeMessage msg);

    @Named("mapSymbol")
    default TradeSymbol mapSymbol(String symbol) {
        try {
            return TradeSymbol.valueOf(symbol);
        } catch (IllegalArgumentException | NullPointerException e) {
            return TradeSymbol.UNKNOWN; // fallback if enum contains UNKNOWN
        }
    }
}