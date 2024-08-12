package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.PharmacistAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacistAccountRepo extends JpaRepository<PharmacistAccount,Long> {
    Optional<PharmacistAccount> findByPharmacistId(Long pharmacistId);
}
