package com.geocodinglocationservices.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicationResponse {
    private String medicationName;
    private String medicationDosage;
    private String days;
    private int medicationQuantity;
    private Double amount;

}
