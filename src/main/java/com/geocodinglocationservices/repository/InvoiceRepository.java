package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.MedicineInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<MedicineInvoice,Long> {
}
