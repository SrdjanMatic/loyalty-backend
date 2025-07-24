package com.example.loyalty.restaurant.domain.restaurant_config;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.james.mime4j.dom.datetime.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReceiptChallengeUsageId.class)
public class ReceiptChallengeUsage {

    @Id
    private String receiptKey;

    @Id
    private Long challengeTemplateId;

    private LocalDateTime usedAt;
}
