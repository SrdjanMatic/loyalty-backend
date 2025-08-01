package com.example.loyalty.dashboard.domain;

import java.sql.Date;

public interface ReceiptCountByDay {
    Date getReceiptDate();
    Long getTotal();
}
