package com.geocodinglocationservices.payload.request;

import lombok.Data;

@Data
public class MedicineUploadRequest {
    private PrescriptionRequest prescriptionRequest;

    private PharmacistIdRequest pharmacistIdRequest;
}
