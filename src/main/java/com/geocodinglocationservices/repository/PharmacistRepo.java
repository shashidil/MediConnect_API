package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacistRepo extends JpaRepository<Pharmacist,Long> {
    List<Pharmacist> findByCity(String city);

    List<Pharmacist> findByStates(String state);
}
