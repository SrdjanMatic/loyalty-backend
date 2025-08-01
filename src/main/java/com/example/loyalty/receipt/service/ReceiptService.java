package com.example.loyalty.receipt.service;

import com.example.loyalty.receipt.domain.GameDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.domain.ReceiptWithWheelDataView;

import java.security.Principal;
import java.util.List;

public interface ReceiptService {
    Receipt create(Receipt receipt);

    List<Receipt> getAllByRestaurantAndUser(Long restaurantId, String name);

    ReceiptWithWheelDataView createReceiptAndProcessFiscalReceipt(String rowData, String username);

    void saveGamePoints(GameDTO gameDTO, Principal principal);
}
