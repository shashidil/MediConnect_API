package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.InvoiceMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRequestRepo extends JpaRepository<InvoiceMedicine,Long> {
}
