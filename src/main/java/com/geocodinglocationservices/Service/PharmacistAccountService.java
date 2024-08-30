package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.PharmacistAccount;
import com.geocodinglocationservices.payload.response.Report.PharmacistAccountDto;

public interface PharmacistAccountService {
    PharmacistAccount createAccount(String email, Long pharmacistId);
    PharmacistAccountDto getAccountByPharmacistId(Long pharmacistId);
}
