package com.parotia.persistor.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeRequest;

public interface TradeEventService {

    Page<TradeEvent> findAll(Pageable pageable);

    TradeEvent create(TradeRequest request);

    Page<TradeEvent> findBySymbol(String s, Pageable pageable);

    Optional<TradeEvent> findById(Long id);

    Optional<TradeEvent> update(Long id, TradeRequest request);

    boolean delete(Long id);
}
