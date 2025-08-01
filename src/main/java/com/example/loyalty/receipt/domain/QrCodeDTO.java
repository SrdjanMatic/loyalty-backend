package com.example.loyalty.receipt.domain;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;


public record QrCodeDTO(
        @NotBlank(message = "Qr code data can not be empty")
        String rowData
) {}
