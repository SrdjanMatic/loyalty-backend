package com.example.loyalty.receipt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptWithWheelDataView {
    private String receiptKey;
    private Long receiptPoints;
    private ArrayList<Long> wheelData;
}
