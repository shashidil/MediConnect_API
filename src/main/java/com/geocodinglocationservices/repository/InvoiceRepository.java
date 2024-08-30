package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.payload.response.InvoiceResponse;
import com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<MedicineInvoice,Long> {
    List<MedicineInvoice> findByCustomer(Customer customer);

    List<MedicineInvoice> findAllByinvoiceNumber(String invoiceId);
    List<MedicineInvoice> findByinvoiceNumber(String invoiceId);

    @Query("SELECT new com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO(md.medicationName, SUM(md.medicationQuantity)) " +
            "FROM MedicineInvoice mi JOIN mi.medications md " +
            "WHERE MONTH(mi.issuedDate) = :month AND YEAR(mi.issuedDate) = :year " +
            "GROUP BY md.medicationName " +
            "ORDER BY SUM(md.medicationQuantity) DESC")
    List<MostSoldMedicineDTO> findTop5MostSoldMedicines(int month, int year);


}
