package com.example.loyalty.receipt.service;

import com.example.loyalty.exceptions.ReceiptProcessingException;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.repository.ReceiptRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Receipt create(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> getAllByRestaurantAndUser(Integer restaurantId, String name) {
        return receiptRepository.getAllByRestaurantIdAndUserId(restaurantId,name);
    }

    @Override
    @Transactional
    public Receipt processFiscalReceipt(String rowData, String username) {
        try {
            String finalUrl = "https://suf.purs.gov.rs/v/?vl=" + rowData;
            URI uri = URI.create(finalUrl);

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

            String html = response.getBody();
            Document doc = Jsoup.parse(html);

            String pib = doc.select("#tinLabel").text();
            String totalAmount = doc.select("#totalAmountLabel").text();
            String invoiceNumber = doc.select("#invoiceNumberLabel").text();

            if (pib.isEmpty() || totalAmount.isEmpty() || invoiceNumber.isEmpty()) {
                throw new IllegalArgumentException("Required fields missing in fiscal receipt HTML.");
            }

            totalAmount = totalAmount.replace(".", "").replace(",", ".");
            BigDecimal decimal;
            try {
                decimal = new BigDecimal(totalAmount);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid amount format: " + totalAmount, nfe);
            }

            Integer points = decimal.multiply(BigDecimal.valueOf(0.01)).intValue();

            Restaurant restaurant = restaurantRepository.findFirstByPib(Integer.valueOf(pib))
                    .orElseThrow(() -> new EntityNotFoundException("Restaurant not found for PIB: " + pib));

            Receipt receipt = Receipt.builder()
                    .restaurant(restaurant)
                    .receiptKey(invoiceNumber)
                    .userId(username)
                    .amount(decimal)
                    .points(points)
                    .build();

            return create(receipt);

        } catch (Exception e) {
            // Log the error with details for debugging
            // logger.error("Error processing fiscal receipt", e);
            throw new ReceiptProcessingException("Failed to process fiscal receipt: " + e.getMessage(), e);
        }
    }
}
