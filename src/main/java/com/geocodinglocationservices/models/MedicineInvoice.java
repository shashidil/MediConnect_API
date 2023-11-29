package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;


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

    private String patientName;

    @Column(nullable = false)
    private String medicationName;

    @Column(nullable = false)
    private String medicationDosage;

    @Column(nullable = false)
    private int medicationQuantity;
    @Column(nullable = false)
    private Double amount;
    private String additionalNotes;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp issuedDate;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;
    @ManyToOne
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

}
