package com.geocodinglocationservices.payload.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceResponse {
    private long id;
    private String pharmacistName;
    private String invoiceNumber;
    private List<MedicationResponse> medications;
    private double distance;
    private Double total;
    private Long pharmacistId;
    private double pharmacistLatitude;
    private double pharmacistLongitude;
    private double customerLatitude;
    private double customerLongitude;


}
