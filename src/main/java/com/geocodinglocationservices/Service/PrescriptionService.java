package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PrescriptionService {
    Prescription storeFile(MultipartFile file, User userId);

    Prescription storeMedicine(User userId, PrescriptionRequest prescriptionRequest);

    List<Prescription> getAllPrescriptionsForPharmacist(Long userId);
}
