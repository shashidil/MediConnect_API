package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.PharmacistService;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.payload.response.PharmacistByCityResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {
    @Autowired
    private PharmacistService pharmacistService;

    @GetMapping("/city/{city}")
    public ResponseEntity<List<PharmacistByCityResponses>> getPharmacistsByCity(@PathVariable String city) {
        List<Pharmacist> pharmacists = pharmacistService.getPharmacistsByCity(city);
        List<PharmacistByCityResponses> response = pharmacists.stream()
                .map(p -> new PharmacistByCityResponses(p.getId(), p.getPharmacyName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<PharmacistByCityResponses>> getPharmacistsByState(@PathVariable String state) {
        List<Pharmacist> pharmacists = pharmacistService.getPharmacistsByState(state);
        List<PharmacistByCityResponses> response = pharmacists.stream()
                .map(p -> new PharmacistByCityResponses(p.getId(), p.getPharmacyName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
