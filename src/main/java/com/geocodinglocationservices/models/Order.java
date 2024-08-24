package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

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
    private String paymentIntentId;

    @Column(nullable = false)
    private String orderStatus = "Awaiting Shipment"; // Default value

    @Column(nullable = false)
    private String paymentStatus; // e.g., "Pending", "Completed", "Failed"

    @Column(nullable = false)
    private Double totalAmount;

    private String trackingNumber;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp orderDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp lastUpdated;

    @Column(name = "invoice_id")
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;


}
