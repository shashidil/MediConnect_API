package com.geocodinglocationservices.payload.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceRequest {
    private List<Medication> medications;
    private String invoiceNumber;
    private Long prescriptionId;
    private Long pharmacistId;
    private Double totalAmount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Medication {
        private String medicationName;
        private String medicationDosage;
        private String days;
        private int medicationQuantity;
        private Double amount;
    }
}
