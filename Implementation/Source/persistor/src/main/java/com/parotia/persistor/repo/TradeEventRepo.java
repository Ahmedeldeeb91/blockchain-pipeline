package com.parotia.persistor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parotia.persistor.model.TradeEvent;

@Repository
public interface TradeEventRepo extends JpaRepository<TradeEvent, Long> {

    TradeEvent findByEventId(Long eventId);
}
