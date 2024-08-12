package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.MedicineInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<MedicineInvoice,Long> {
    List<MedicineInvoice> findByCustomer(Customer customer);

    List<MedicineInvoice> findByinvoiceNumber(String invoiceId);
}
