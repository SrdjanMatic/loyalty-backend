package com.example.loyalty.restaurant.service.vip_restaurant;


import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.domain.VipRestaurantUserView;
import com.example.loyalty.restaurant.domain.VipRestaurantView;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

public interface VipRestaurantService {
    List<VipRestaurantView> findAll();

    VipRestaurantView create(VipRestaurantDTO vipRestaurantDTO, Principal principal);

    void delete(Long id, Principal principal);

    VipRestaurantView update(Long id, @Valid VipRestaurantDTO vipRestaurantDTO, Principal principal);

    List<VipRestaurantUserView> findAllByUser(Principal principal);
}
