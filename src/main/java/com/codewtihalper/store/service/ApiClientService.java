package com.codewtihalper.store.service;

import com.codewtihalper.store.dto.Client1Response;
import com.codewtihalper.store.dto.Client2Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClientService {

    private final RestTemplate restTemplate;
    private final LoggingService loggingService;

    private static final String CLIENT1_URL = "https://6f0028f3-b77d-451e-8471-7ed5480d2e3d.mock.pstmn.io/client1";
    private static final String CLIENT2_URL = "https://6f0028f3-b77d-451e-8471-7ed5480d2e3d.mock.pstmn.io/client2";

    @Autowired
    public ApiClientService(RestTemplate restTemplate, LoggingService loggingService) {
        this.restTemplate = restTemplate;
        this.loggingService = loggingService;
    }

    /**
     * Client1 API'den GET isteği ile veri çeker
     */
    public Client1Response fetchClient1Data() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Request log
            loggingService.logRequest("client1", CLIENT1_URL, "GET", null);

            ResponseEntity<Client1Response> response = restTemplate.exchange(
                    CLIENT1_URL, HttpMethod.GET, entity, Client1Response.class);

            // Response log
            loggingService.logResponse("client1", response.getBody(), response.getStatusCodeValue());

            return response.getBody();

        } catch (Exception e) {
            loggingService.logError("client1", "GET", e.getMessage());
            throw new RuntimeException("Client1 API call failed: " + e.getMessage());
        }
    }

    /**
     * Client2 API'ye POST isteği ile veri çeker
     */
    public Client2Response fetchClient2Data() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // POST body (boş JSON gönderebiliriz)
            String requestBody = "{}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Request log
            loggingService.logRequest("client2", CLIENT2_URL, "POST", requestBody);

            ResponseEntity<Client2Response> response = restTemplate.exchange(
                    CLIENT2_URL, HttpMethod.GET, entity, Client2Response.class);

            // Response log
            loggingService.logResponse("client2", response.getBody(), response.getStatusCodeValue());

            return response.getBody();

        } catch (Exception e) {
            loggingService.logError("client2", "POST", e.getMessage());
            throw new RuntimeException("Client2 API call failed: " + e.getMessage());
        }
    }
}
