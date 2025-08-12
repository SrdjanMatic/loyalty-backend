package com.example.loyalty.coupon.repository;

import com.example.loyalty.base.repository.BaseRepository;
import com.example.loyalty.coupon.domain.Coupon;
import com.example.loyalty.coupon.domain.CouponView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends BaseRepository<Coupon, Long> {
    List<CouponView> findByRestaurantId(Long restaurantId);
}