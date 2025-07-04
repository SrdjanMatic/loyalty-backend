package com.example.loyalty.coupon.repository;

import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByRestaurantId(Long restaurantId);
}