package com.example.loyalty.receipt.repository;

import com.example.loyalty.receipt.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> getAllByRestaurantIdAndUserId(Integer restaurantId, String name);
}