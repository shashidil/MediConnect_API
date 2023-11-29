package com.geocodinglocationservices.payload.request;

import com.geocodinglocationservices.models.Pharmacist;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceRequest {
    private String medicationName;
    private String medicationDosage;
    private int medicationQuantity;
    private Double amount;
    private String additionalNotes;
    private Long prescriptionId;
    private Long pharmacistId;
}
