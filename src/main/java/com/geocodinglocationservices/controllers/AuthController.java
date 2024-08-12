package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.LoginRequest;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.payload.request.SignupRequestPatient;
import com.geocodinglocationservices.payload.request.SignupRequestPharmacist;
import com.geocodinglocationservices.payload.response.JwtResponse;
import com.geocodinglocationservices.payload.response.MessageResponse;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import com.geocodinglocationservices.security.jwt.JwtUtils;
import com.geocodinglocationservices.security.services.UserDetailsImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  private AuthService authService;


  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

    @GetMapping("/hello") // This maps the /hello path to this method
    public String helloWorld() {
      return "Hello World"; // Return the "Hello World" string
    }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
    String username = null;
    String email = null;
    if (signUpRequest.getSignupRequestPatient() != null && isPatientDataNotEmpty(signUpRequest.getSignupRequestPatient())){
      SignupRequestPatient patient = signUpRequest.getSignupRequestPatient();
      username = patient.getUsername();
      email = patient.getEmail();
    }else if (signUpRequest.getSignupRequestPharmacist() != null && isPharmacistDataNotEmpty(signUpRequest.getSignupRequestPharmacist())) {
      SignupRequestPharmacist pharmacist = signUpRequest.getSignupRequestPharmacist();
      username = pharmacist.getUsername();
      email = pharmacist.getEmail();
    }
    else {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Request are Empty!"));
    }
    if (authService.existsByUsername(username)) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (authService.existsByEmail(email)) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }
    User registeredUser = authService.registerUser(signUpRequest);
    if (registeredUser == null){
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

  }

  private boolean isPatientDataNotEmpty(SignupRequestPatient patient) {
    return !StringUtils.isBlank(patient.getUsername()) && !StringUtils.isBlank(patient.getEmail());
  }

  private boolean isPharmacistDataNotEmpty(SignupRequestPharmacist pharmacist) {
    return !StringUtils.isBlank(pharmacist.getUsername()) && !StringUtils.isBlank(pharmacist.getEmail());
  }
}
