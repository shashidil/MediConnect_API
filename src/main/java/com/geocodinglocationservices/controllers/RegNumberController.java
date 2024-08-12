package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.payload.response.RegNumberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class RegNumberController {
    private final RestTemplate restTemplate;

    public RegNumberController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/checkRegNumber")
    public ResponseEntity<Boolean> checkRegNumber(@RequestParam String regNumber) {
        String baseUrl = "https://medicalcouncil.lk/renewal/verify/verify_reg";
        String url = String.format("%s?cat_id=2&reg_no=%s", baseUrl, regNumber);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().equals("null")) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
        }
    }

