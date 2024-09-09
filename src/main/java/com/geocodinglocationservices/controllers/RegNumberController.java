package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.payload.response.RegNumberResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegNumberController {

    private final RestTemplate restTemplate;

    public RegNumberController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/checkRegNumber")
    public ResponseEntity<?> checkRegNumber(@RequestParam String regNumber) {
        String baseUrl = "https://medicalcouncil.lk/renewal/verify/verify_reg";

        // Prepare form data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set form data (cat_id=2 is constant, and reg_no is passed as input)
        Map<String, String> formData = new HashMap<>();
        formData.put("cat_id", "2"); // Always 2
        formData.put("reg_no", regNumber); // Input registration number

        // Convert form data to a URL-encoded format
        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (requestBody.length() > 0) {
                requestBody.append("&");
            }
            requestBody.append(entry.getKey()).append("=").append(entry.getValue());
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // Send the POST request to the external API
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);

        // Return the response from the external API
        return ResponseEntity.ok(response.getBody());
    }
}
