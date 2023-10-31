package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRequestRepo extends JpaRepository<Medicine,Long> {
}
