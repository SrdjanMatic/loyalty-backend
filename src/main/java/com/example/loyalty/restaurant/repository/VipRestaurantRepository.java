package com.example.loyalty.restaurant.repository;

import com.example.loyalty.base.repository.BaseRepository;
import com.example.loyalty.restaurant.domain.VipRestaurant;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VipRestaurantRepository extends BaseRepository<VipRestaurant, Long> {
    Optional<Object> findByRestaurantId(@NotNull Long aLong);
}
