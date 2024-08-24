package com.geocodinglocationservices.payload.response;

import lombok.Data;
import java.util.Date;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String paymentMethod;
    private String paymentIntentId;
    private String orderStatus;
    private String paymentStatus;
    private String invoiceNumber;
    private Double totalAmount;
    private Date orderDate;
}
