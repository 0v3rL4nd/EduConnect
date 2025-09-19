package com.piattaforme.educonnect.business.webservice;

import com.piattaforme.educonnect.persistence.entity.Payment;
import com.piattaforme.educonnect.persistence.entity.PaymentMethod;
import com.piattaforme.educonnect.persistence.entity.PaymentStatus;
import com.piattaforme.educonnect.business.dto.PaymentResponseDTO;
import com.piattaforme.educonnect.business.ejb.PaymentSessionBean;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;

@WebService(name = "PaymentService",
        targetNamespace = "http://webservice.educonnect.com/",
        serviceName = "PaymentWebService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class PaymentWebService {

    private PaymentSessionBean paymentSessionBean;
    PaymentResponseDTO response = new PaymentResponseDTO();

    /**
     * Processa pagamento tramite provider esterno
     */
    @WebMethod(operationName = "processPayment")
    @WebResult(name = "paymentResponse")
    public PaymentResponseDTO processPayment(
            @WebParam(name = "bookingId") Long bookingId,
            @WebParam(name = "paymentMethod") String paymentMethod,
            @WebParam(name = "amount") BigDecimal amount,
            @WebParam(name = "currency") String currency,
            @WebParam(name = "customerEmail") String customerEmail,
            @WebParam(name = "externalTransactionId") String externalTransactionId) {

        try {
            PaymentMethod method = PaymentMethod.valueOf(paymentMethod.toUpperCase());

            Payment payment = paymentSessionBean.processPayment(
                    bookingId, method, externalTransactionId);


            response.setSuccess(true);
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getStatus().name());
            response.setAmount(payment.getAmount());
            response.setMessage("Pagamento processato con successo");

            return response;

        } catch (Exception e) {
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setSuccess(false);
            response.setMessage("Errore nel processamento: " + e.getMessage());
            response.setStatus(PaymentStatus.FAILED.name());

            return response;
        }
    }

    /**
     * Verifica stato pagamento
     */
    @WebMethod(operationName = "checkPaymentStatus")
    @WebResult(name = "paymentStatus")
    public String checkPaymentStatus(
            @WebParam(name = "transactionId") String transactionId) {

        PaymentStatus status = paymentSessionBean.checkPaymentStatus(transactionId);
        return status != null ? status.name() : "NOT_FOUND";
    }

    /**
     * Processa rimborso
     */
    @WebMethod(operationName = "processRefund")
    @WebResult(name = "refundResponse")
    public PaymentResponseDTO processRefund(
            @WebParam(name = "paymentId") Long paymentId,
            @WebParam(name = "reason") String reason,
            @WebParam(name = "amount") BigDecimal refundAmount) {

        try {
            Payment refund = paymentSessionBean.processRefund(paymentId, reason);

            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setSuccess(true);
            response.setTransactionId(refund.getTransactionId());
            response.setStatus(refund.getStatus().name());
            response.setAmount(refund.getAmount());
            response.setMessage("Rimborso processato con successo");

            return response;

        } catch (Exception e) {
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setSuccess(false);
            response.setMessage("Errore nel rimborso: " + e.getMessage());

            return response;
        }
    }
}