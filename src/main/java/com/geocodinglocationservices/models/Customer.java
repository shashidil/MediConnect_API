package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "customer")
//@DiscriminatorValue("customer")
public class Customer extends User {
    private String lastName;
    private String firstName;
    @NotBlank

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]+$", message = "Invalid phone number format")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;
    @NotEmpty(message = "User location is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String addressLine1;

    @NotEmpty(message = "User city is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String city;

    @NotEmpty(message = "User states is required")
    @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
    private String states;

    @NotEmpty(message = "Postal code is required")
    @Size(max = 10, message = "Pharmacy location can have a maximum of 100 characters")
    private String postalCode;

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicineInvoice> invoices;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

//    private Set<Role> role;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;



}
