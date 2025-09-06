package com.parotia.persistor.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeRequest;
import com.parotia.persistor.repo.TradeEventRepo;
import com.parotia.persistor.util.TradeEventMapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
class TradeEventServiceImpl implements TradeEventService {

    TradeEventRepo repo;
    TradeEventMapper mapper;

    public TradeEvent create(TradeRequest request) {
        TradeEvent event = mapper.toTradeEvent(request);
        return repo.save(event);
    }

    public Page<TradeEvent> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<TradeEvent> findBySymbol(String symbol, Pageable pageable) {
        return repo.findBySymbol(mapper.mapSymbol(symbol),
                pageable);
    }

    public Optional<TradeEvent> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<TradeEvent> update(Long id, TradeRequest request) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setEventTime(request.getEventTime());
                    existing.setPrice(request.getPrice());
                    existing.setQuantity(request.getQuantity());
                    existing.setTradeTime(request.getTradeTime());
                    existing.setIsMarket(request.isMarketMaker());
                    existing.setIsIgnore(request.isIgnored());
                    existing.setSymbol(mapper.mapSymbol(request.getSymbol()));
                    return repo.save(existing);
                });
    }

    public boolean delete(Long id) {
        return repo.findById(id)
                .map(entity -> {
                    repo.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}
