package com.geocodinglocationservices.payload.response.Report;

import lombok.Data;

import java.util.Date;

@Data
public class OrderReportDto {
    private Long id;
    private String orderNumber;
    private String orderStatus;
    private String paymentStatus;
    private String invoiceNumber;
    private Double totalAmount;
    private String trackingNumber;
    private Date orderDate;
}
