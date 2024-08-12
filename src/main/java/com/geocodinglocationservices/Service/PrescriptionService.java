package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.PharmacistIdRequest;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import com.geocodinglocationservices.payload.response.PrescriptionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PrescriptionService {
    Prescription storeFile(MultipartFile file, Long userId,PharmacistIdRequest pharmacistIdRequest);

    Prescription storeMedicine(User userId, PrescriptionRequest prescriptionRequest,PharmacistIdRequest pharmacistIdRequest);

    List<PrescriptionDTO> getAllPrescriptionsForPharmacist(Long pharmacistId);
}
