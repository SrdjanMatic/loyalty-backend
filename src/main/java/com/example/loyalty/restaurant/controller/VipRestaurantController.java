package com.example.loyalty.restaurant.controller;

import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.domain.CompanyView;
import com.example.loyalty.restaurant.domain.VipRestaurant;
import com.example.loyalty.restaurant.domain.VipRestaurantDTO;
import com.example.loyalty.restaurant.domain.VipRestaurantView;
import com.example.loyalty.restaurant.service.vip_restaurant.VipRestaurantServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vip-restaurants")
@RequiredArgsConstructor
public class VipRestaurantController {

    private final VipRestaurantServiceImpl service;

    @PostMapping
    public VipRestaurantView create(@RequestBody @Valid VipRestaurantDTO dto, Principal principal) {
        return service.create(dto, principal);
    }

    @GetMapping
    public List<VipRestaurantView> getAll(Principal principal) {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public VipRestaurantView update(@PathVariable Long id,
                              @RequestBody @Valid VipRestaurantDTO vipRestaurantDTO, Principal principal) {
        return service.update(id, vipRestaurantDTO, principal);
    }
}
