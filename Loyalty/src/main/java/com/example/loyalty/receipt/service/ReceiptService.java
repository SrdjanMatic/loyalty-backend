package com.example.loyalty.receipt.service;

import com.example.loyalty.receipt.domain.Receipt;

import java.util.List;

public interface ReceiptService {
    Receipt create(Receipt receipt);

    List<Receipt> getAllByRestaurantAndUser(Integer restaurantId, String name);

    Receipt processFiscalReceipt(String rowData, String username);
}
