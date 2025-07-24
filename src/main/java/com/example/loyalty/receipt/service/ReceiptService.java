package com.example.loyalty.receipt.service;

import com.example.loyalty.receipt.domain.GameDTO;
import com.example.loyalty.receipt.domain.Receipt;
import com.example.loyalty.receipt.domain.ReceiptWithWheelDataView;

import java.util.List;

public interface ReceiptService {
    Receipt create(Receipt receipt);

    List<Receipt> getAllByRestaurantAndUser(Long restaurantId, String name);

    ReceiptWithWheelDataView processFiscalReceipt(String rowData, String username);

    void addGamePoints(GameDTO gameDTO, String name);
}
