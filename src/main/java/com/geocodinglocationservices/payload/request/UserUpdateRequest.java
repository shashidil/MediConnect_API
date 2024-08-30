package com.geocodinglocationservices.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;

    // Common fields for both Customer and Pharmacist
    private String addressLine1;
    private String city;
    private String states;
    private String postalCode;
    private Double latitude;
    private Double longitude;

    // Fields specific to Customer
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Fields specific to Pharmacist
    private String pharmacyName;

}
