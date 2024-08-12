package com.geocodinglocationservices.payload.request;

import com.geocodinglocationservices.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupRequest {

  private SignupRequestPatient signupRequestPatient;
  private SignupRequestPharmacist signupRequestPharmacist;
//  private String username;
//  private String lastName;
//  private String firstName;
//  @Email
//  private String email;
//  @NotBlank
//  @Size(max = 120)
//  private String password;
//  private String phoneNumber;
//  private String pharmacyName;
//  private String regNumber;
//  @NotEmpty(message = "Pharmacy location is required")
//  @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
//  private String addressLine1;
//
//  @NotEmpty(message = "Pharmacy location is required")
//  @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
//  private String city;
//
//  @NotEmpty(message = "Pharmacy location is required")
//  @Size(max = 100, message = "Pharmacy location can have a maximum of 100 characters")
//  private String states;
//
//  @NotEmpty(message = "Postal code is required")
//  @Size(max = 10, message = "Pharmacy location can have a maximum of 100 characters")
//  private String postalCode;
//  private Set<String> role;




}
