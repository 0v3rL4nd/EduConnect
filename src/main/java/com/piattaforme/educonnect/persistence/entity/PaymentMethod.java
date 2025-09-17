package com.piattaforme.educonnect.persistence.entity;

public enum PaymentMethod {
    CREDIT_CARD("Carta di Credito"),
    DEBIT_CARD("Carta di Debito"),
    PAYPAL("PayPal"),
    BANK_TRANSFER("Bonifico Bancario"),
    WALLET("Portafoglio Digitale");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
