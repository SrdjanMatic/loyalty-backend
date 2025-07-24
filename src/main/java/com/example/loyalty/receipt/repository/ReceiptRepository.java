package com.example.loyalty.receipt.repository;

import com.example.loyalty.receipt.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    List<Receipt> getAllByRestaurantIdAndUserId(Long restaurantId, String name);

    Receipt findByReceiptKey(String receiptKey);

    @Query(value = """
    SELECT *
    FROM receipt r
    WHERE r.restaurant_id = :restaurantId
      AND r.user_id = :userId
      AND r.receipt_date > :afterDate
      AND NOT EXISTS (
          SELECT 1
          FROM receipt_challenge_usage u
          WHERE u.receipt_key = r.receipt_key AND u.challenge_template_id= :challengeId
      )
""", nativeQuery = true)
    List<Receipt> findAllByRestaurantIdAndUserIdAndReceiptDateAfterOrderByReceiptDateDesc(
            @Param("restaurantId") Long restaurantId,
            @Param("challengeId") Long challengeId,
            @Param("userId") String userId,
            @Param("afterDate") LocalDateTime afterDate
    );

    @Query(value = """
    SELECT COUNT(*)
    FROM receipt r
    WHERE r.restaurant_id = :restaurantId
      AND r.user_id = :userId
      AND r.receipt_date > :afterDate
      AND NOT EXISTS (
          SELECT 1
          FROM receipt_challenge_usage u
          WHERE u.receipt_key = r.receipt_key AND u.challenge_template_id= :challengeId
      )
""", nativeQuery = true)
    long countUnusedReceiptsAfterDate(
            @Param("restaurantId") Long restaurantId,
            @Param("challengeId") Long challengeId,
            @Param("userId") String userId,
            @Param("afterDate") LocalDateTime afterDate
    );

}