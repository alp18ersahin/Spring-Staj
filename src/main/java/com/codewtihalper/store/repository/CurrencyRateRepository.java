package com.codewtihalper.store.repository;

import com.codewtihalper.store.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    // Belirli bir currency için tüm API'lardan gelen son kurları getir
    @Query("SELECT cr FROM CurrencyRate cr WHERE cr.targetCurrency = :currency " +
            "AND cr.createdAt = (SELECT MAX(cr2.createdAt) FROM CurrencyRate cr2 " +
            "WHERE cr2.sourceApi = cr.sourceApi AND cr2.targetCurrency = :currency)")
    List<CurrencyRate> findLatestRatesByCurrency(@Param("currency") String currency);

    // Belirli API'dan gelen son kurları getir
    List<CurrencyRate> findBySourceApiOrderByCreatedAtDesc(String sourceApi);

    // Belirli currency ve API için son kuru getir
    CurrencyRate findFirstBySourceApiAndTargetCurrencyOrderByCreatedAtDesc(
            String sourceApi, String targetCurrency);
}
