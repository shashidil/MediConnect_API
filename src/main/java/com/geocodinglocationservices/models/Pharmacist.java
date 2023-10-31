package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

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
//    private Set<Role> role;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;





}
