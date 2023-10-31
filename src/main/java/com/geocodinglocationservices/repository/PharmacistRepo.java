package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacistRepo extends JpaRepository<Pharmacist,Long> {
}
