package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.Service.PasswordResetService;
import com.geocodinglocationservices.models.PasswordResetToken;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.*;
import com.geocodinglocationservices.payload.response.*;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import com.geocodinglocationservices.security.jwt.JwtUtils;
import com.geocodinglocationservices.security.services.ReminderService;
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
import java.util.Map;
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
  @Autowired
  private PasswordResetService resetService;


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
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
    String username = null;
    String email = null;

    // Check if the patient data is not empty
    if (signUpRequest.getSignupRequestPatient() != null && isPatientDataNotEmpty(signUpRequest.getSignupRequestPatient())) {
      SignupRequestPatient patient = signUpRequest.getSignupRequestPatient();
      username = patient.getUsername();
      email = patient.getEmail();
    }
    // Check if the pharmacist data is not empty
    else if (signUpRequest.getSignupRequestPharmacist() != null && isPharmacistDataNotEmpty(signUpRequest.getSignupRequestPharmacist())) {
      SignupRequestPharmacist pharmacist = signUpRequest.getSignupRequestPharmacist();
      username = pharmacist.getUsername();
      email = pharmacist.getEmail();
    }
    // If both are empty, return an error
    else {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Request data is empty!"));
    }

    // Validate if the username already exists
    if (authService.existsByUsername(username)) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    // Validate if the email already exists
    if (authService.existsByEmail(email)) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Register the user
    User registeredUser = authService.registerUser(signUpRequest);
    if (registeredUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }


  @PutMapping("/{userId}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest updateRequest) throws IOException {

      UserDTO updatedUser = authService.updateUser(userId, updateRequest);
      return ResponseEntity.ok(updatedUser);  // Return the updated user object
    }



  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
    try {
      UserDetailsDto userDetailsDto = authService.getUserDetails(userId);
      return ResponseEntity.ok(userDetailsDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error retrieving user details: " + e.getMessage());
    }
  }

  @GetMapping("/notification/{userId}")
  public ResponseEntity<List<NotificationMessage>> getNotification(@PathVariable Long userId) {
    List<NotificationMessage> notificationMessage = authService.handleNotificationInLogin(userId);
    return ResponseEntity.ok(notificationMessage);

  }

  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(@RequestParam String email) {
    resetService.createPasswordResetToken(email);
    return ResponseEntity.ok("Password reset email sent.");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token,@RequestParam String newPassword) {

    String resetPassword = resetService.resetPassword(token, newPassword);
    return ResponseEntity.ok(resetPassword);
  }


  private boolean isPatientDataNotEmpty(SignupRequestPatient patient) {
    return patient != null && !StringUtils.isBlank(patient.getUsername()) && !StringUtils.isBlank(patient.getEmail());
  }

  // Helper method to check if pharmacist data is not empty
  private boolean isPharmacistDataNotEmpty(SignupRequestPharmacist pharmacist) {
    return pharmacist != null && !StringUtils.isBlank(pharmacist.getUsername()) && !StringUtils.isBlank(pharmacist.getEmail());
  }
}
