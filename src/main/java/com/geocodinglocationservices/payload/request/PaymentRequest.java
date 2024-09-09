package com.geocodinglocationservices.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long invoiceId;
    private String orderNumber;
    private Long pharmacistId;
    private Long customerId;
    private String paymentMethod;
    private Double amount;
}
