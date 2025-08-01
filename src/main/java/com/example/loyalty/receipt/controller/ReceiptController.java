package com.example.loyalty.receipt.controller;

import com.example.loyalty.receipt.domain.GameDTO;
import com.example.loyalty.receipt.domain.QrCodeDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.domain.ReceiptWithWheelDataView;
import com.example.loyalty.receipt.service.ReceiptServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<Receipt> getAll(@PathVariable Long restaurantId, Principal principal) {
        return service.getAllByRestaurantAndUser(restaurantId, principal.getName());
    }

    @PostMapping("/create-receipt")
    public ReceiptWithWheelDataView createReceipt(@RequestBody @Valid QrCodeDTO vl, Principal principal) {
        return service.createReceiptAndProcessFiscalReceipt(vl.rowData(), principal.getName());
    }

    @PostMapping("/game-points")
    public ResponseEntity<Void> saveGamePoints(@RequestBody @Valid GameDTO gameDTO, Principal principal) {
        service.saveGamePoints(gameDTO, principal);
        return ResponseEntity.noContent().build();
    }


}
