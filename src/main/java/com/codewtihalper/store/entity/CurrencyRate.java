package com.codewtihalper.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_api")
    private String sourceApi; // "client1" or "client2"

    @Column(name = "base_currency")
    private String baseCurrency; // "USD"

    @Column(name = "target_currency")
    private String targetCurrency; // "EUR", "GBP", etc.

    @Column(name = "rate", precision = 10, scale = 6)
    private BigDecimal rate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public CurrencyRate() {
        this.createdAt = LocalDateTime.now();
    }

    public CurrencyRate(String sourceApi, String baseCurrency, String targetCurrency, BigDecimal rate) {
        this();
        this.sourceApi = sourceApi;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSourceApi() { return sourceApi; }
    public void setSourceApi(String sourceApi) { this.sourceApi = sourceApi; }

    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
