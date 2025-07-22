package com.codewtihalper.store.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrencyComparisonResult {

    private String currency;
    private BigDecimal client1Rate;
    private BigDecimal client2Rate;
    private BigDecimal bestRate;
    private String bestSource;
    private LocalDateTime comparisonTime;

    // Constructors
    public CurrencyComparisonResult() {
        this.comparisonTime = LocalDateTime.now();
    }

    public CurrencyComparisonResult(String currency, BigDecimal client1Rate,
                                    BigDecimal client2Rate) {
        this();
        this.currency = currency;
        this.client1Rate = client1Rate;
        this.client2Rate = client2Rate;

        // En iyi kuru belirle (daha yüksek olan)
        if (client1Rate != null && client2Rate != null) {
            if (client1Rate.compareTo(client2Rate) >= 0) {
                this.bestRate = client1Rate;
                this.bestSource = "client1";
            } else {
                this.bestRate = client2Rate;
                this.bestSource = "client2";
            }
        } else if (client1Rate != null) {
            this.bestRate = client1Rate;
            this.bestSource = "client1";
        } else if (client2Rate != null) {
            this.bestRate = client2Rate;
            this.bestSource = "client2";
        }
    }

    // Getters and Setters
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getClient1Rate() { return client1Rate; }
    public void setClient1Rate(BigDecimal client1Rate) { this.client1Rate = client1Rate; }

    public BigDecimal getClient2Rate() { return client2Rate; }
    public void setClient2Rate(BigDecimal client2Rate) { this.client2Rate = client2Rate; }

    public BigDecimal getBestRate() { return bestRate; }
    public void setBestRate(BigDecimal bestRate) { this.bestRate = bestRate; }

    public String getBestSource() { return bestSource; }
    public void setBestSource(String bestSource) { this.bestSource = bestSource; }

    public LocalDateTime getComparisonTime() { return comparisonTime; }
    public void setComparisonTime(LocalDateTime comparisonTime) {
        this.comparisonTime = comparisonTime;
    }
}
