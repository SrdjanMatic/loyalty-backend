package com.example.loyalty.restaurant.repository;

import com.example.loyalty.restaurant.domain.VipRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VipRestaurantRepository extends JpaRepository<VipRestaurant, Long> {
}
