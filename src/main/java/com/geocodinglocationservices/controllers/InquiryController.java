package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.InquiryService;
import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.payload.request.InquiryRequestDTO;
import com.geocodinglocationservices.payload.response.InquiryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {
    @Autowired
    private InquiryService inquiryService;

    @PostMapping("/create")
    public InquiryResponseDTO createInquiry(@RequestBody InquiryRequestDTO inquiryDTO) {
        return inquiryService.createInquiry(inquiryDTO);
    }

    @GetMapping("/customer-inquiries")
    public List<InquiryResponseDTO> getCustomerInquiries() {
        return inquiryService.getInquiriesBySenderType(Customer.class);
    }

    @GetMapping("/pharmacist-inquiries")
    public List<InquiryResponseDTO> getPharmacistInquiries() {
        return inquiryService.getInquiriesBySenderType(Pharmacist.class);
    }

    @PutMapping("/update-status/{id}")
    public InquiryResponseDTO updateInquiryStatus(@PathVariable Long id, @RequestParam String status) {
        return inquiryService.updateInquiryStatus(id, status);
    }
}
