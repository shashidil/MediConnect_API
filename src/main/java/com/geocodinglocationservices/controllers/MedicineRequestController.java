package com.geocodinglocationservices.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geocodinglocationservices.Service.MedicineRequestService;
import com.geocodinglocationservices.Service.PrescriptionService;
import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.PharmacistIdRequest;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import com.geocodinglocationservices.payload.response.PrescriptionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-requests")
public class MedicineRequestController {

    @Autowired
    private PrescriptionService prescriptionService;
   @PostMapping("/uploadPrescription/{id}")
   public ResponseEntity<String> uploadFile(
           @RequestParam("file") MultipartFile file,
           @PathVariable("id") Long userId,
           @RequestParam("pharmacistIds") String pharmacistIdsJson) {
       try {
           ObjectMapper objectMapper = new ObjectMapper();
           List<Long> pharmacistIds = objectMapper.readValue(pharmacistIdsJson, new TypeReference<List<Long>>() {});
           PharmacistIdRequest pharmacistIdRequest = new PharmacistIdRequest();
           pharmacistIdRequest.setPharmacistId(pharmacistIds);

           Prescription prescription = prescriptionService.storeFile(file, userId, pharmacistIdRequest);
           return ResponseEntity.ok("File uploaded successfully");
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Failed to upload file: " + e.getMessage());
       }
   }

    @PostMapping("/uploadMedicine/{id}")
    public ResponseEntity<String> uploadMedicine(@PathVariable("id")User userId,@RequestBody PrescriptionRequest prescriptionRequest,
                                                 @Valid @RequestBody PharmacistIdRequest pharmacistIdRequest) {
        Prescription prescription = prescriptionService.storeMedicine(userId,prescriptionRequest,pharmacistIdRequest);
        return ResponseEntity.ok("Medicine uploaded successfully: ");
    }

   // @PreAuthorize("hasRole('ROLE_PHARMACIST')")
    @GetMapping("/{pharmacistId}")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions(@PathVariable("pharmacistId")Long pharmacistId) {
        List<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptionsForPharmacist(pharmacistId);
        return ResponseEntity.ok(prescriptions);

    }



}
