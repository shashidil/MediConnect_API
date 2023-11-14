package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.MedicineRequestService;
import com.geocodinglocationservices.Service.PrescriptionService;
import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.MedicineRequest;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-requests")
public class MedicineRequestController {

    @Autowired
    private MedicineRequestService medicineRequest;

    @Autowired
    private PrescriptionService prescriptionService;


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/uploadPrescription/{id}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id")User userId) {
        Prescription prescription = prescriptionService.storeFile(file, userId);
        return ResponseEntity.ok("File uploaded successfully: ");
    }
    @PostMapping("/uploadMedicine/{id}")
    public ResponseEntity<String> uploadMedicine(@PathVariable("id")User userId,@RequestBody PrescriptionRequest prescriptionRequest) {
        Prescription prescription = prescriptionService.storeMedicine(userId,prescriptionRequest);
        return ResponseEntity.ok("Medicine uploaded successfully: ");
    }

    @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<List<Prescription>> getAllPrescriptions(@PathVariable("id")Long userId) {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptionsForPharmacist(userId);
        return ResponseEntity.ok(prescriptions);
    }


}
