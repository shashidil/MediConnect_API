package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.PharmacistAccountService;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.PharmacistAccount;
import com.geocodinglocationservices.repository.PharmacistAccountRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PharmacistAccountServiceImpl implements PharmacistAccountService {
    @Value("${stripe.api.key}")
    private String apiKey;

    @Autowired
    private  PharmacistRepo pharmacistRepo;
    @Autowired
    private PharmacistAccountRepo pharmacistAccountRepo;

    @Override
    public PharmacistAccount createAccount(String email, Long pharmacistId) {
        Stripe.apiKey = apiKey;

        Map<String, Object> accountParams = new HashMap<>();
        accountParams.put("type", "standard");
        accountParams.put("country", "US");
        accountParams.put("email", email);

        try {
            Account account = Account.create(accountParams);

            Pharmacist pharmacist = pharmacistRepo.findById(pharmacistId)
                    .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

            PharmacistAccount pharmacistAccount = new PharmacistAccount();
            pharmacistAccount.setStripeAccountId(account.getId());
            pharmacistAccount.setEmail(email);
            pharmacistAccount.setPharmacist(pharmacist);
            return pharmacistAccountRepo.save(pharmacistAccount);

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PharmacistAccount getAccountByPharmacistId(Long pharmacistId) {
        return pharmacistAccountRepo.findByPharmacistId(pharmacistId)
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist account not found"));
    }
}
