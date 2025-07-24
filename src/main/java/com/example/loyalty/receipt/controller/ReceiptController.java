package com.example.loyalty.receipt.controller;

import com.example.loyalty.receipt.domain.GameDTO;
import com.example.loyalty.receipt.domain.QrCodeDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.domain.ReceiptWithWheelDataView;
import com.example.loyalty.receipt.service.ReceiptServiceImpl;
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
    public ResponseEntity<List<Receipt>> getAll(@PathVariable Long restaurantId, Principal principal) {
        return ResponseEntity.ok(service.getAllByRestaurantAndUser(restaurantId, principal.getName()));
    }

    @PostMapping("/create-receipt")
    public ReceiptWithWheelDataView createReceipt(@RequestBody QrCodeDTO vl, Principal principal) {
        return service.processFiscalReceipt(vl.getRowData(), principal.getName());
    }

    @PostMapping("/game-points")
    public void addGamePoints(@RequestBody GameDTO gameDTO, Principal principal) {
        service.addGamePoints(gameDTO, principal.getName());
    }


}
