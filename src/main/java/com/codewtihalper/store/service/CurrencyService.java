package com.codewtihalper.store.service;

import com.codewtihalper.store.dto.Client1Response;
import com.codewtihalper.store.dto.Client2Response;
import com.codewtihalper.store.dto.CurrencyComparisonResult;
import com.codewtihalper.store.entity.CurrencyRate;
import com.codewtihalper.store.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private final CurrencyRateRepository currencyRateRepository;
    private final ApiClientService apiClientService;

    @Autowired
    public CurrencyService(CurrencyRateRepository currencyRateRepository,
                           ApiClientService apiClientService) {
        this.currencyRateRepository = currencyRateRepository;
        this.apiClientService = apiClientService;
    }

    /**
     * Client1'den veri çekip veritabanına kaydet
     */
    public Client1Response fetchAndSaveClient1Data() {
        Client1Response response = apiClientService.fetchClient1Data();

        if (response != null && response.getConversionRates() != null) {
            // Her currency rate'i veritabanına kaydet
            response.getConversionRates().forEach((currency, rate) -> {
                CurrencyRate currencyRate = new CurrencyRate(
                        "client1",
                        response.getBaseCode(),
                        currency,
                        BigDecimal.valueOf(rate)
                );
                currencyRateRepository.save(currencyRate);
            });
        }

        return response;
    }

    /**
     * Client2'den veri çekip veritabanına kaydet
     */
    public Client2Response fetchAndSaveClient2Data() {
        Client2Response response = apiClientService.fetchClient2Data();

        if (response != null) {
            // Client2'deki her currency'yi kaydet
            Map<String, Double> rates = new HashMap<>();
            rates.put("USD", response.getUsd());
            rates.put("EUR", response.getEur());
            rates.put("GBP", response.getGbp());
            rates.put("CAD", response.getCad());
            rates.put("CHF", response.getChf());
            rates.put("RUB", response.getRub());

            rates.forEach((currency, rate) -> {
                if (rate != null) {
                    CurrencyRate currencyRate = new CurrencyRate(
                            "client2",
                            "USD", // Client2 de USD base gibi görünüyor
                            currency,
                            BigDecimal.valueOf(rate)
                    );
                    currencyRateRepository.save(currencyRate);
                }
            });
        }

        return response;
    }

    /**
     * İki API'yı karşılaştır ve en iyi kurları bul
     */
    public List<CurrencyComparisonResult> compareRates() {
        List<CurrencyComparisonResult> results = new ArrayList<>();

        // Ortak currency'leri bul (Client2'de olanlar)
        String[] commonCurrencies = {"USD", "EUR", "GBP", "CAD", "CHF", "RUB"};

        for (String currency : commonCurrencies) {
            // Son kurları getir
            CurrencyRate client1Rate = currencyRateRepository
                    .findFirstBySourceApiAndTargetCurrencyOrderByCreatedAtDesc("client1", currency);

            CurrencyRate client2Rate = currencyRateRepository
                    .findFirstBySourceApiAndTargetCurrencyOrderByCreatedAtDesc("client2", currency);

            // Karşılaştırma sonucu oluştur
            BigDecimal rate1 = client1Rate != null ? client1Rate.getRate() : null;
            BigDecimal rate2 = client2Rate != null ? client2Rate.getRate() : null;

            CurrencyComparisonResult result = new CurrencyComparisonResult(currency, rate1, rate2);
            results.add(result);
        }

        return results;
    }

    /**
     * Belirli bir currency için en iyi kuru bul
     */
    public CurrencyComparisonResult getBestRateForCurrency(String currency) {
        CurrencyRate client1Rate = currencyRateRepository
                .findFirstBySourceApiAndTargetCurrencyOrderByCreatedAtDesc("client1", currency);

        CurrencyRate client2Rate = currencyRateRepository
                .findFirstBySourceApiAndTargetCurrencyOrderByCreatedAtDesc("client2", currency);

        BigDecimal rate1 = client1Rate != null ? client1Rate.getRate() : null;
        BigDecimal rate2 = client2Rate != null ? client2Rate.getRate() : null;

        return new CurrencyComparisonResult(currency, rate1, rate2);
    }
}