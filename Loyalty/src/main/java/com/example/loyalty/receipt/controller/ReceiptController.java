package com.example.loyalty.receipt.controller;

import com.example.loyalty.exceptions.ReceiptProcessingException;
import com.example.loyalty.receipt.domain.QrCodeDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.service.ReceiptServiceImpl;
import com.example.loyalty.restaurant.domain.Restaurant;
import com.example.loyalty.restaurant.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptServiceImpl service;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<Receipt>> getAll(@PathVariable Integer restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getAllByRestaurantAndUser(restaurantId, principal.getName()));
    }

    @PostMapping("/view")
    public ResponseEntity<Receipt> getFiscalReceipt(@RequestBody QrCodeDTO vl, Principal principal) {
        Receipt saved = service.processFiscalReceipt(vl.getRowData(), principal.getName());
        return ResponseEntity.ok(saved);

    }


}
