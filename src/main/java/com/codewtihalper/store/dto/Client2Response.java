package com.codewtihalper.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client2Response {

    @JsonProperty("USD")
    private Double usd;

    @JsonProperty("EUR")
    private Double eur;

    @JsonProperty("GPB")
    private Double gbp;

    @JsonProperty("CAD")
    private Double cad;

    @JsonProperty("CHF")
    private Double chf;

    @JsonProperty("RUB")
    private Double rub;

    // Constructors
    public Client2Response() {}

    // Getters and Setters
    public Double getUsd() { return usd; }
    public void setUsd(Double usd) { this.usd = usd; }

    public Double getEur() { return eur; }
    public void setEur(Double eur) { this.eur = eur; }

    public Double getGbp() { return gbp; }
    public void setGbp(Double gbp) { this.gbp = gbp; }

    public Double getCad() { return cad; }
    public void setCad(Double cad) { this.cad = cad; }

    public Double getChf() { return chf; }
    public void setChf(Double chf) { this.chf = chf; }

    public Double getRub() { return rub; }
    public void setRub(Double rub) { this.rub = rub; }
}
