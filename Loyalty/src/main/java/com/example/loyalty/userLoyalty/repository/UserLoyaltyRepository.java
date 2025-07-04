package com.example.loyalty.userLoyalty.repository;

import com.example.loyalty.userLoyalty.domain.LoyaltyId;
import com.example.loyalty.userLoyalty.domain.UserLoyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoyaltyRepository extends JpaRepository<UserLoyalty, LoyaltyId> {
}