package com.geocodinglocationservices.payload.response;

import lombok.Data;
import java.util.Date;

@Data
public class OrderResponse {
    private Long id;
    private String paymentMethod;
    private String paymentIntentId;
    private String paymentStatus;
    private Double totalAmount;
    private Date orderDate;
}
