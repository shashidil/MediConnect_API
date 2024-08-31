package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.PharmacistAccountService;
import com.geocodinglocationservices.models.PharmacistAccount;
import com.geocodinglocationservices.payload.response.Report.PharmacistAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacist-account")
public class PharmacistAccountController {
    @Autowired
    private PharmacistAccountService pharmacistAccountService;

    @PostMapping()
    public PharmacistAccountDto createAccount(@RequestParam String email, @RequestParam Long pharmacistId) {
        return pharmacistAccountService.createAccount(email, pharmacistId);
    }

    @GetMapping("/{pharmacistId}")
    public PharmacistAccountDto getAccount(@PathVariable Long pharmacistId) {
        return pharmacistAccountService.getAccountByPharmacistId(pharmacistId);
    }
}
