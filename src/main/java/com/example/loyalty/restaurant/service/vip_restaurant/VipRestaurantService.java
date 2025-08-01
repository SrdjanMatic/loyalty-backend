package com.example.loyalty.restaurant.service.vip_restaurant;


import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;

import java.security.Principal;
import java.util.List;

public interface VipRestaurantService {
    List<VipRestaurant> findAll();

    VipRestaurant create(VipRestaurantDTO vipRestaurantDTO, Principal principal);
}
