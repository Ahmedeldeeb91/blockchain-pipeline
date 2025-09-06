package com.parotia.persistor.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Entity
@Table(name = "trade_event")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TradeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    Long eventId; // corresponds to Binance tradeId

    @Column
    Long eventTime;

    @Enumerated(EnumType.STRING)
    TradeSymbol symbol;

    BigDecimal quantity;

    BigDecimal price;

    long tradeTime;

    Boolean isMarket;

    Boolean isIgnore;

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    Instant modifiedAt;
}
