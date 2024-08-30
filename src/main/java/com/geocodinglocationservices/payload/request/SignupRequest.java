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




}
