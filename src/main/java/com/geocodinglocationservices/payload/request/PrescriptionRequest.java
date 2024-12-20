package com.geocodinglocationservices.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionRequest {
    private String medicationName;
    private int medicationQuantity;
}
