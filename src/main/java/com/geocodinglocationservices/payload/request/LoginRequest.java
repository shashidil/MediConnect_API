package com.geocodinglocationservices.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@NotEmpty(message = "Patient user is required")
	@Size(max = 100, message = "Patient user can have a maximum of 100 characters")
  private String username;

	@NotEmpty(message = "Patient password is required")
	@Size(max = 100, message = "Patient password can have a maximum of 100 characters")
	private String password;


}
