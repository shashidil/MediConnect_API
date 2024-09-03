package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.PharmacistAccountService;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.PharmacistAccount;
import com.geocodinglocationservices.payload.response.Report.PharmacistAccountDto;
import com.geocodinglocationservices.repository.PharmacistAccountRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PharmacistAccountServiceImpl implements PharmacistAccountService {
    @Value("${stripe.api.key}")
    private String apiKey;

    @Autowired
    private  PharmacistRepo pharmacistRepo;
    @Autowired
    private PharmacistAccountRepo pharmacistAccountRepo;

    private ModelMapper modelMapper= new ModelMapper();

    @Override
    public PharmacistAccountDto createAccount(String email, Long pharmacistId) {
        Stripe.apiKey = apiKey;

        Map<String, Object> accountParams = new HashMap<>();
        accountParams.put("type", "standard");
        accountParams.put("country", "US");
        accountParams.put("email", email);

        // Account account = Account.create(accountParams);

        Pharmacist pharmacist = pharmacistRepo.findById(pharmacistId)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

        PharmacistAccount pharmacistAccount = new PharmacistAccount();
        // pharmacistAccount.setStripeAccountId(account.getId());
        pharmacistAccount.setStripeAccountId("123365");
        pharmacistAccount.setEmail(email);
        pharmacistAccount.setPharmacist(pharmacist);
        PharmacistAccount saved = pharmacistAccountRepo.save(pharmacistAccount);
        return modelMapper.map(saved, PharmacistAccountDto.class);
    }

    @Override
    public PharmacistAccountDto getAccountByPharmacistId(Long pharmacistId) {
        Optional<PharmacistAccount> pharmacistAccount = pharmacistAccountRepo.findByPharmacistId(pharmacistId);
        PharmacistAccountDto dto = new PharmacistAccountDto();
        dto.setId(pharmacistAccount.get().getId());
        dto.setEmail(pharmacistAccount.get().getEmail());
        return  dto;
    }

    @Override
    public PharmacistAccountDto updateAccountById(Long pharmacistId,String email) {
        PharmacistAccount pharmacistAccount = pharmacistAccountRepo.findByPharmacistId(pharmacistId)
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist account not found"));
        pharmacistAccount.setEmail(email);
        PharmacistAccount saved = pharmacistAccountRepo.save(pharmacistAccount);
        return modelMapper.map(saved, PharmacistAccountDto.class);
    }
}
