package com.codewtihalper.store.controller;

import com.codewtihalper.store.dto.Client1Response;
import com.codewtihalper.store.dto.Client2Response;
import com.codewtihalper.store.dto.CurrencyComparisonResult;
import com.codewtihalper.store.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * GET /client1 - Client1 API'den veri çek (DOĞRU)
     */
    @GetMapping("/client1")
    public ResponseEntity<?> getClient1Data() {
        try {
            Client1Response response = currencyService.fetchAndSaveClient1Data();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching Client1 data: " + e.getMessage());
        }
    }

    /**
     * POST /client2 - Client2 API'den veri çek (DOĞRU)
     */
    @PostMapping("/client2")
    public ResponseEntity<?> postClient2Data() {
        try {
            Client2Response response = currencyService.fetchAndSaveClient2Data();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching Client2 data: " + e.getMessage());
        }
    }

    /**
     * POST /client1 - HATALI İSTEK (Hata vermeli)
     */
    @PostMapping("/client1")
    public ResponseEntity<?> postClient1Data() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("ERROR: POST method not allowed for /client1. Use GET method.");
    }

    /**
     * GET /client2 - HATALI İSTEK (Hata vermeli)
     */
    @GetMapping("/client2")
    public ResponseEntity<?> getClient2Data() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("ERROR: GET method not allowed for /client2. Use POST method.");
    }

    /**
     * Tüm kurları karşılaştır ve en iyilerini göster
     */
    @GetMapping("/compare")
    public ResponseEntity<List<CurrencyComparisonResult>> compareAllRates() {
        try {
            List<CurrencyComparisonResult> results = currencyService.compareRates();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Belirli bir currency için en iyi kuru göster
     */
    @GetMapping("/best-rate/{currency}")
    public ResponseEntity<CurrencyComparisonResult> getBestRate(@PathVariable String currency) {
        try {
            CurrencyComparisonResult result = currencyService.getBestRateForCurrency(currency.toUpperCase());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Test endpoint - Hem client1 hem client2'den veri çekip karşılaştır
     */
    @GetMapping("/fetch-and-compare")
    public ResponseEntity<List<CurrencyComparisonResult>> fetchAndCompare() {
        try {
            // Önce her iki API'dan da veri çek
            currencyService.fetchAndSaveClient1Data();
            currencyService.fetchAndSaveClient2Data();

            // Sonra karşılaştır
            List<CurrencyComparisonResult> results = currencyService.compareRates();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
