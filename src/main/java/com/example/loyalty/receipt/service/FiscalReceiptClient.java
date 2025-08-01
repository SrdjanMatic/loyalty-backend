package com.example.loyalty.receipt.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@Log4j2
public class FiscalReceiptClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public Document fetchFiscalReceiptHtml(String rowData) {
        String finalUrl = "https://suf.purs.gov.rs/v/?vl=" + rowData;
        URI uri = URI.create(finalUrl);

        HttpHeaders headers = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                String.class
        );

        log.debug("Fiscal receipt HTTP status: {}", response.getStatusCode());
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            log.error("Failed to fetch fiscal receipt HTML. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
            throw new IllegalStateException("Failed to fetch fiscal receipt HTML.");
        }

        return Jsoup.parse(response.getBody());
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.set("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.set("Cache-Control", "max-age=0");
        headers.set("Priority", "u=0, i");
        headers.set("Sec-CH-UA", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
        headers.set("Sec-CH-UA-Mobile", "?1");
        headers.set("Sec-CH-UA-Platform", "\"Android\"");
        headers.set("Sec-Fetch-Dest", "document");
        headers.set("Sec-Fetch-Mode", "navigate");
        headers.set("Sec-Fetch-Site", "none");
        headers.set("Sec-Fetch-User", "?1");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Mobile Safari/537.36");
        headers.set("Cookie", "localization=sr-Cyrl-RS");
        return headers;
    }
}