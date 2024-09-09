package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String orderNumber;


    @Column(nullable = false)
    private String orderStatus = "Awaiting Shipment";

    @Column(nullable = false)
    private String paymentStatus;

    @Column(nullable = false)
    private Double totalAmount;

    private String trackingNumber;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp orderDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp lastUpdated;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private MedicineInvoice invoice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

}
