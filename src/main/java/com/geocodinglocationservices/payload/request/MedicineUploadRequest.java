package com.geocodinglocationservices.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineUploadRequest {
    private PrescriptionRequest prescriptionRequest;
    private PharmacistIdRequest pharmacistIdRequest;
}
