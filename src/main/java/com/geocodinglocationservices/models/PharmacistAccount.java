package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pharmacist_accounts")
public class PharmacistAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stripeAccountId;

    @Column(nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

}
