package com.geocodinglocationservices.payload.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceResponse {
    private String pharmacistName;
    private String invoiceNumber;
    private List<MedicationResponse> medications;
    private String additionalNotes;
    private double distance;
    private Double total;
    private Long pharmacistId;
    private double pharmacistLatitude;
    private double pharmacistLongitude;
    private double customerLatitude;
    private double customerLongitude;


}
