package com.geocodinglocationservices.payload.response;

import lombok.Data;

@Data
public class UserDetailsDto {
    private String username;
    private String email;

    // Common fields for both Customer and Pharmacist
    private String addressLine1;
    private String city;
    private String states;
    private String postalCode;

    // Fields specific to Customer
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Fields specific to Pharmacist
    private String pharmacyName;

    // Field to identify the role
    private String role;
}
