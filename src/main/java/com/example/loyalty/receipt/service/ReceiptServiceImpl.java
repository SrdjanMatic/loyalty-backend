package com.example.loyalty.receipt.service;

import com.example.loyalty.exceptions.ReceiptProcessingException;
import com.example.loyalty.receipt.domain.GameDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.domain.ReceiptWithWheelDataView;
import com.example.loyalty.receipt.repository.ReceiptRepository;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.domain.restaurant_config.ChallengeTemplate;
import com.example.loyalty.restaurant.domain.restaurant_config.ReceiptChallengeUsage;
import com.example.loyalty.restaurant.repository.ChallengeTemplateRepository;
import com.example.loyalty.restaurant.repository.ReceiptChallengeUsageRepository;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import com.example.loyalty.user_loyalty.domain.UserLoyalty;
import com.example.loyalty.user_loyalty.repository.UserLoyaltyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final UserLoyaltyRepository useLoyaltyRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ChallengeTemplateRepository challengeTemplateRepository;
    private final ReceiptChallengeUsageRepository receiptChallengeUsageRepository;

    @Override
    public Receipt create(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> getAllByRestaurantAndUser(Long restaurantId, String name) {
        return receiptRepository.getAllByRestaurantIdAndUserId(restaurantId, name);
    }

    @Override
    @Transactional
    public ReceiptWithWheelDataView processFiscalReceipt(String rowData, String userId) {
        try {
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

            String html = response.getBody();
            Document doc = Jsoup.parse(html);

            String pib = doc.select("#tinLabel").text();
            String totalAmount = doc.select("#totalAmountLabel").text();
//            String invoiceNumber = doc.select("#invoiceNumberLabel").text();
            String invoiceNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String receiptDate = doc.select("#sdcDateTimeLabel").text();


            if (pib.isEmpty() || totalAmount.isEmpty() || invoiceNumber.isEmpty()) {
                throw new IllegalArgumentException("Required fields missing in fiscal receipt HTML.");
            }

            Optional<Receipt> optionalReceipt = receiptRepository.findById(invoiceNumber);
            if (optionalReceipt.isPresent()) {
                throw new IllegalStateException("Receipt with ID '" + invoiceNumber + "' already exists.");
            }

            totalAmount = totalAmount.replace(".", "").replace(",", ".");
            BigDecimal decimal;
            try {
                decimal = new BigDecimal(totalAmount);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid amount format: " + totalAmount, nfe);
            }

            Long points = decimal.multiply(BigDecimal.valueOf(0.01)).longValue();

            Restaurant restaurant = restaurantRepository.findFirstByPib(pib)
                    .orElseThrow(() -> new EntityNotFoundException("Restaurant not found for PIB: " + pib));

            saveUserLoyalty(userId, restaurant, points);
            Receipt savedReceipt = saveReceipt(userId, restaurant, invoiceNumber, decimal, points, receiptDate);

            return ReceiptWithWheelDataView.builder()
                    .receiptKey(savedReceipt.getReceiptKey())
                    .wheelData(buildWheelData(savedReceipt.getPoints()))
                    .build();


        } catch (Exception e) {
            // Log the error with details for debugging
            // logger.error("Error processing fiscal receipt", e);
            throw new ReceiptProcessingException("Failed to process fiscal receipt: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void addGamePoints(GameDTO gameDTO, String name) {
        Receipt receipt = receiptRepository.findByReceiptKey(gameDTO.receiptKey());
        receipt.setGamePoints(gameDTO.gamePoints());
        receiptRepository.save(receipt);

        ChallengeTemplate challengeTemplate = challengeTemplateRepository.findById(gameDTO.challengeId()).orElseThrow(() -> new RuntimeException("Template not found with id: " + gameDTO.challengeId()));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterDate = now.minusDays(challengeTemplate.getPeriod());
        List<Receipt> allReceiptInChallengeReceiptPeriod = receiptRepository.findAllByRestaurantIdAndUserIdAndReceiptDateAfterOrderByReceiptDateDesc(receipt.getRestaurant().getId(),challengeTemplate.getId(), name, afterDate);


        List<ReceiptChallengeUsage> usages = allReceiptInChallengeReceiptPeriod.stream()
                .limit(challengeTemplate.getVisitsRequired())
                .map(receiptChallenged -> ReceiptChallengeUsage.builder()
                        .challengeTemplateId(gameDTO.challengeId())
                        .receiptKey(receiptChallenged.getReceiptKey())
                        .usedAt(now)
                        .build())
                .toList();

        receiptChallengeUsageRepository.saveAll(usages);


        UserLoyalty userLoyalty = useLoyaltyRepository.findByRestaurantIdAndUserId(receipt.getRestaurant().getId(), name);

        long newTotalPoints = userLoyalty.getTotalPoints() + gameDTO.gamePoints();

        userLoyalty.setAvailablePoints(userLoyalty.getAvailablePoints() + gameDTO.gamePoints());
        userLoyalty.setTotalPoints(newTotalPoints);
        checkPromotion(receipt.getRestaurant(), newTotalPoints, userLoyalty);
        useLoyaltyRepository.save(userLoyalty);
    }

    private void saveUserLoyalty(String userId, Restaurant restaurant, Long earnedPoints) {
        UserLoyalty userLoyalty = useLoyaltyRepository
                .findByRestaurantIdAndUserId(restaurant.getId(), userId);

        long newTotalPoints = userLoyalty.getTotalPoints() + earnedPoints;

        userLoyalty.setAvailablePoints(userLoyalty.getAvailablePoints() + earnedPoints);
        userLoyalty.setTotalPoints(newTotalPoints);

        checkPromotion(restaurant, newTotalPoints, userLoyalty);

        useLoyaltyRepository.save(userLoyalty);
    }

    private static void checkPromotion(Restaurant restaurant, long newTotalPoints, UserLoyalty userLoyalty) {
        if (newTotalPoints > restaurant.getVipCouponLimit() && !userLoyalty.getLevel().equals(UserLoyalty.UserLoyaltyLevel.VIP)) {
            userLoyalty.setLevel(UserLoyalty.UserLoyaltyLevel.PROMOTED_TO_VIP);
        } else if (newTotalPoints > restaurant.getPremiumCouponLimit() && !userLoyalty.getLevel().equals(UserLoyalty.UserLoyaltyLevel.PREMIUM) && !userLoyalty.getLevel().equals(UserLoyalty.UserLoyaltyLevel.VIP)) {
            userLoyalty.setLevel(UserLoyalty.UserLoyaltyLevel.PROMOTED_TO_PREMIUM);
        }
    }

    private Receipt saveReceipt(String userId, Restaurant restaurant, String invoiceNumber, BigDecimal decimal, Long points, String receiptDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy. HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(receiptDate, formatter);


        Receipt receipt = Receipt.builder()
                .restaurant(restaurant)
                .receiptKey(invoiceNumber)
                .userId(userId)
                .amount(decimal)
                .receiptDate(dateTime)
                .points(points)
                .build();

        return receiptRepository.save(receipt);
    }

    public ArrayList<Long> buildWheelData(Long point) {
        Random rand = new Random();

        ArrayList<Long> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long randomNum = rand.nextLong(point - 1) + 1;
            randomNumbers.add(randomNum);
        }

        return randomNumbers;
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
