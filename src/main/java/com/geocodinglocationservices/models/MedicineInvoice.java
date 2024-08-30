package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
public class MedicineInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invoiceNumber;

    private String patientName;

    // Use @ElementCollection to map a list of MedicationDetail objects
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "medication_details", joinColumns = @JoinColumn(name = "invoice_id"))
    private List<MedicationDetail> medications = new ArrayList<>();

    @Column(nullable = false)
    private Double total;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp issuedDate;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


}
