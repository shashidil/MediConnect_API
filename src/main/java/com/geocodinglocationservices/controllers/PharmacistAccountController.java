package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.PharmacistAccountService;
import com.geocodinglocationservices.models.PharmacistAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacist-account")
public class PharmacistAccountController {
    @Autowired
    private PharmacistAccountService pharmacistAccountService;

    @PostMapping("/create")
    public PharmacistAccount createAccount(@RequestParam String email, @RequestParam Long pharmacistId) {
        return pharmacistAccountService.createAccount(email, pharmacistId);
    }

    @GetMapping("/{pharmacistId}")
    public PharmacistAccount getAccount(@PathVariable Long pharmacistId) {
        return pharmacistAccountService.getAccountByPharmacistId(pharmacistId);
    }
}
