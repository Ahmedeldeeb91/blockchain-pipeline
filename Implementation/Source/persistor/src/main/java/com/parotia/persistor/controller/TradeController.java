package com.parotia.persistor.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeRequest;
import com.parotia.persistor.service.TradeEventService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/trades")
public class TradeController {
    TradeEventService tradeEventService;

    @PostMapping
    public ResponseEntity<TradeEvent> createTrade(@RequestBody TradeRequest request) {
        TradeEvent saved = tradeEventService.create(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Page<TradeEvent>> getTrades(
            @RequestParam Optional<String> symbol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TradeEvent> result = symbol
                .map(s -> tradeEventService.findBySymbol(s, pageable))
                .orElseGet(() -> tradeEventService.findAll(pageable));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeEvent> getTradeById(@PathVariable Long id) {
        return tradeEventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeEvent> updateTrade(
            @PathVariable Long id,
            @RequestBody TradeRequest request) {
        return tradeEventService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long id) {
        boolean deleted = tradeEventService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
