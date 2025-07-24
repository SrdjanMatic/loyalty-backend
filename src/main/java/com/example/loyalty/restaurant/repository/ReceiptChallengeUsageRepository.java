package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.restaurant_config.ReceiptChallengeUsage;
import com.example.loyalty.restaurant.domain.restaurant_config.ReceiptChallengeUsageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptChallengeUsageRepository extends JpaRepository<ReceiptChallengeUsage, ReceiptChallengeUsageId> {
}
