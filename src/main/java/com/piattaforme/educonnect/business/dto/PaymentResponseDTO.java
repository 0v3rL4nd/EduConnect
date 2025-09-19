package com.piattaforme.educonnect.business.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {

    private boolean success;
    private String transactionId;
    private String status;
    private BigDecimal amount;
    private String message;
    private String errorCode;

    public PaymentResponseDTO() {
    }


}