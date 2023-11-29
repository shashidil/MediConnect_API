package com.geocodinglocationservices.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceResponse {
    private String patientName;
    private String medicationName;
    private String medicationDosage;
    private int medicationQuantity;
    private Double amount;
    private String additionalNotes;

}
