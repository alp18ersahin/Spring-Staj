package com.codewtihalper.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class Client1Response {

    private String result;

    @JsonProperty("faq")
    private String faq;

    @JsonProperty("terms_of_use")
    private String termsOfUse;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("base_code")
    private String baseCode;

    @JsonProperty("conversion_rates")
    private Map<String, Double> conversionRates;

    // Constructors
    public Client1Response() {}

    // Getters and Setters
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getFaq() { return faq; }
    public void setFaq(String faq) { this.faq = faq; }

    public String getTermsOfUse() { return termsOfUse; }
    public void setTermsOfUse(String termsOfUse) { this.termsOfUse = termsOfUse; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getBaseCode() { return baseCode; }
    public void setBaseCode(String baseCode) { this.baseCode = baseCode; }

    public Map<String, Double> getConversionRates() { return conversionRates; }
    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }
}