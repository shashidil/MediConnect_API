package com.geocodinglocationservices.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestPatient {
    private String username;
    private String lastName;
    private String firstName;
    @Email
    @NotEmpty(message = "Patient email is required")
    private String email;
   @NotBlank
    @Size(max = 120)
    private String password;
    @NotEmpty(message = "Patient phone number is required")
    private String phoneNumber;
    @NotEmpty(message = "Patient location is required")
    @Size(max = 100, message = "Patient location can have a maximum of 100 characters")
    private String addressLine1;

    @NotEmpty(message = "Patient location is required")
    @Size(max = 100, message = "Patient location can have a maximum of 100 characters")
    private String city;

   @NotEmpty(message = "Patient location is required")
    @Size(max = 100, message = "Patient location can have a maximum of 100 characters")
    private String states;

   @NotEmpty(message = "Postal code is required")
    @Size(max = 10, message = "Patient location can have a maximum of 100 characters")
    private String postalCode;
    private Set<String> role;

}
