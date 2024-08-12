package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.PharmacistAccount;

public interface PharmacistAccountService {
    PharmacistAccount createAccount(String email, Long pharmacistId);
    PharmacistAccount getAccountByPharmacistId(Long pharmacistId);
}
