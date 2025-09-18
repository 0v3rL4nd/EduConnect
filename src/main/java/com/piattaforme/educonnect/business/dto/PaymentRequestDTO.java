package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class PaymentRequestDTO {

    // Getters and Setters
    @NotNull(message = "ID prenotazione è obbligatorio")
    private Long bookingId;

    @NotNull(message = "Metodo pagamento è obbligatorio")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Importo è obbligatorio")
    private BigDecimal amount;

    private String currency = "EUR";

    private String cardToken; // Per pagamenti carta
    private String paypalToken; // Per PayPal
    private String externalTransactionId;

    // Constructors
    public PaymentRequestDTO() {}

}