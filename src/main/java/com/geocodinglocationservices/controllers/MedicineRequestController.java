package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.MedicineRequestService;
import com.geocodinglocationservices.payload.request.MedicineRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicine-requests")
public class MedicineRequestController {

    @Autowired
    private MedicineRequestService medicineRequest;

    @PostMapping("/send")
    public ResponseEntity<?> sendMedicineRequest(@RequestBody @Valid MedicineRequest requestDTO) {
        MedicineRequest createdRequest = medicineRequest.sendMedicineRequest(requestDTO);
        return ResponseEntity.ok(createdRequest);
    }

}
