package com.codewtihalper.store.service;

import com.codewtihalper.store.entity.ApiLog;
import com.codewtihalper.store.repository.ApiLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private static final String LOG_FILE_NAME = "api-requests-responses.log";

    private final ApiLogRepository apiLogRepository;
    private final ObjectMapper objectMapper;
    private final Path logFilePath;

    @Autowired
    public LoggingService(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
        this.objectMapper = new ObjectMapper();
        this.logFilePath = Paths.get(LOG_FILE_NAME);

        // Log dosyasını oluştur
        createLogFileIfNotExists();
    }

    private void createLogFileIfNotExists() {
        try {
            if (!Files.exists(logFilePath)) {
                Files.createFile(logFilePath);
                writeToFile("=== API REQUEST/RESPONSE LOG FILE ===");
                writeToFile("Created at: " + LocalDateTime.now());
                writeToFile("==========================================");
            }
        } catch (IOException e) {
            logger.error("Log dosyası oluşturulamadı: {}", e.getMessage());
        }
    }

    /**
     * Request loglar
     */
    public void logRequest(String apiName, String url, String method, String requestBody) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Database'e kaydet
        ApiLog apiLog = new ApiLog();
        apiLog.setApiName(apiName);
        apiLog.setRequestUrl(url);
        apiLog.setRequestMethod(method);
        apiLog.setRequestBody(requestBody);
        apiLogRepository.save(apiLog);

        // Console log
        logger.info("API Request - {}: {} {} - Body: {}", apiName, method, url, requestBody);

        // Dosyaya yaz
        String logEntry = String.format("\n[%s] REQUEST - %s\n" +
                        "Method: %s\n" +
                        "URL: %s\n" +
                        "Body: %s\n" +
                        "----------------------------------------",
                timestamp, apiName.toUpperCase(), method, url, requestBody);

        writeToFile(logEntry);
    }

    /**
     * Response loglar
     */
    public void logResponse(String apiName, Object responseBody, int statusCode) {
        try {
            String responseJson = objectMapper.writeValueAsString(responseBody);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Database update
            ApiLog latestLog = apiLogRepository.findByApiNameOrderByCreatedAtDesc(apiName)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (latestLog != null) {
                latestLog.setResponseBody(responseJson);
                latestLog.setResponseStatus(statusCode);
                apiLogRepository.save(latestLog);
            }

            // Console log
            logger.info("API Response - {}: Status {} - Body: {}", apiName, statusCode, responseJson);

            // Dosyaya yaz
            String logEntry = String.format("[%s] RESPONSE - %s\n" +
                            "Status: %d\n" +
                            "Body: %s\n" +
                            "========================================\n",
                    timestamp, apiName.toUpperCase(), statusCode, responseJson);

            writeToFile(logEntry);

        } catch (JsonProcessingException e) {
            logger.error("Response serialization error: {}", e.getMessage());
        }
    }

    /**
     * Error loglar
     */
    public void logError(String apiName, String method, String errorMessage) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        logger.error("API Error - {}: {} - Error: {}", apiName, method, errorMessage);

        // Dosyaya yaz
        String logEntry = String.format("\n[%s] ERROR - %s\n" +
                        "Method: %s\n" +
                        "Error: %s\n" +
                        "========================================\n",
                timestamp, apiName.toUpperCase(), method, errorMessage);

        writeToFile(logEntry);

        // Database
        ApiLog apiLog = new ApiLog();
        apiLog.setApiName(apiName);
        apiLog.setRequestMethod(method);
        apiLog.setResponseBody("ERROR: " + errorMessage);
        apiLog.setResponseStatus(500);
        apiLogRepository.save(apiLog);
    }

    /**
     * Dosyaya yazma işlemi
     */
    private void writeToFile(String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(content);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logger.error("Dosyaya yazma hatası: {}", e.getMessage());
        }
    }
}