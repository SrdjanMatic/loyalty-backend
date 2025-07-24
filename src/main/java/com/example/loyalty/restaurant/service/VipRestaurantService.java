package com.example.loyalty.restaurant.service;


import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;

import java.util.List;

public interface VipRestaurantService {
    List<VipRestaurant> getAll();

    VipRestaurant create(VipRestaurantDTO vipRestaurantDTO);
}
