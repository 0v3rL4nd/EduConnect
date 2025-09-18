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

    // Constructors
    public PaymentResponseDTO() {
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTransactionId() {
        return transactionId;
    }


}