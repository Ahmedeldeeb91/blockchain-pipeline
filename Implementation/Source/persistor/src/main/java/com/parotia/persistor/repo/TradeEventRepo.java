package com.parotia.persistor.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeSymbol;

@Repository
public interface TradeEventRepo extends JpaRepository<TradeEvent, Long> {

    TradeEvent findByEventId(Long eventId);

    @Query("SELECT t FROM TradeEvent t WHERE t.symbol = :symbol")
    Page<TradeEvent> findBySymbol(@Param("symbol") TradeSymbol symbol,
            Pageable pageable);
}
