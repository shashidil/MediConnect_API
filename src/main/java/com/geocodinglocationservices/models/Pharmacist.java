package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pharmacist")
//@DiscriminatorValue("pharmacist")
public class Pharmacist extends User {

    private String pharmacyName;
    private String regNumber;
    @NotEmpty(message = "Pharmacy location is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String addressLine1;

    @NotEmpty(message = "Pharmacy location is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String city;

    @NotEmpty(message = "Pharmacy location is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String states;

    @NotEmpty(message = "Postal code is required")
    @Size(max = 10, message = "Pharmacy location can have a maximum of 100 characters")
    private String postalCode;

    private Double latitude;
    private Double longitude;

    @ManyToMany(mappedBy = "pharmacists")
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "pharmacist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "pharmacist", cascade = CascadeType.ALL, orphanRemoval = true)
    private PharmacistAccount pharmacistAccount;






}
