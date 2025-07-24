package com.example.loyalty.receipt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptWithWheelDataView {
    private String receiptKey;
    private ArrayList<Long> wheelData;
}
