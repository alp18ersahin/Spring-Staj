package com.codewtihalper.store.repository;

import com.codewtihalper.store.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    // Belirli API için logları getir
    List<ApiLog> findByApiNameOrderByCreatedAtDesc(String apiName);

    // Belirli tarih aralığındaki logları getir
    List<ApiLog> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate, LocalDateTime endDate);

    // Son N adet logu getir
    List<ApiLog> findTop10ByOrderByCreatedAtDesc();
}
