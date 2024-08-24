package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.payload.request.SignupRequestPatient;
import com.geocodinglocationservices.payload.request.SignupRequestPharmacist;
import com.geocodinglocationservices.payload.response.UserDTO;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import com.geocodinglocationservices.utill.GeocodingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    PharmacistRepo pharmacistRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper = new ModelMapper();


    public User registerUser(SignupRequest signUpRequest) throws IOException {
        Set<String> strRoles;
        String fullAddress;
        GeocodingService.LatLng coordinates;
        if (isPatientDataValid(signUpRequest)) {
            SignupRequestPatient patient = signUpRequest.getSignupRequestPatient();
            strRoles = patient.getRole();
        } else if (isPharmacistDataValid(signUpRequest)) {
            SignupRequestPharmacist pharmacist = signUpRequest.getSignupRequestPharmacist();
            strRoles = pharmacist.getRole();
        } else {
            // Handle the case where neither patient nor pharmacist role is provided
            throw new RuntimeException("Error: Role is not provided!");
        }

//        if (signUpRequest.getSignupRequestPatient() != null&& isPatientDataNotEmpty(signUpRequest.getSignupRequestPatient())){
//            SignupRequestPatient patient = signUpRequest.getSignupRequestPatient();
//            strRoles = patient.getRole();
//        } else if (signUpRequest.getSignupRequestPharmacist() != null  && isPharmacistDataNotEmpty(signUpRequest.getSignupRequestPharmacist())) {
//            SignupRequestPharmacist pharmacist = signUpRequest.getSignupRequestPharmacist();
//            strRoles=pharmacist.getRole();
//        } else {
//            // Handle the case where neither patient nor pharmacist role is provided
//             throw new RuntimeException("Error: Role is not provided!");
//        }

        Set<Role> roles = new HashSet<>();
        User user = null;


        if (strRoles == null || strRoles.isEmpty()) {
            throw new RuntimeException("Error: Role is not found.");
        } else {
            for (String strRole : strRoles) {
                switch (strRole) {
                    case "admin":
                        // Handle admin role
                        // Create an Admin class if needed and save it
                        break;
                    case "Pharmacist":
                        Role modRole = roleRepository.findByName(ERole.ROLE_PHARMACIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        Pharmacist pharmacist = modelMapper.map(signUpRequest.getSignupRequestPharmacist(), Pharmacist.class);

                        // Concatenate address components to form a full address
                         fullAddress = String.format("%s, %s, %s",
                                pharmacist.getAddressLine1(),
                                pharmacist.getCity(),
                                pharmacist.getStates());
                                //pharmacist.getPostalCode());

                        // Use the full address for geocoding
                         coordinates = GeocodingService.getCoordinates(fullAddress);
                        //NominatimGeocodingService.LatLng coordinates = NominatimGeocodingService.getCoordinates(fullAddress);

                        if (coordinates != null) {
                            pharmacist.setLatitude(coordinates.latitude);
                            pharmacist.setLongitude(coordinates.longitude);

                        }

                        pharmacist.setRoles(roles);
                        pharmacistRepo.save(pharmacist);
                        user = pharmacist;
                        user.setUsername(signUpRequest.getSignupRequestPharmacist().getUsername());
                        user.setEmail(signUpRequest.getSignupRequestPharmacist().getEmail());
                        user.setPassword(passwordEncoder.encode(signUpRequest.getSignupRequestPharmacist().getPassword()));
                        break;
                    case "customer":
                        Role cusRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(cusRole);
                        Customer customer = modelMapper.map(signUpRequest.getSignupRequestPatient(), Customer.class);

                        // Concatenate address components to form a full address
                         fullAddress = String.format("%s, %s, %s",
                                customer.getAddressLine1(),
                                customer.getCity(),
                                customer.getStates());
                        //pharmacist.getPostalCode());

                        // Use the full address for geocoding
                         coordinates = GeocodingService.getCoordinates(fullAddress);
                        //NominatimGeocodingService.LatLng coordinates = NominatimGeocodingService.getCoordinates(fullAddress);

                        if (coordinates != null) {
                            customer.setLatitude(coordinates.latitude);
                            customer.setLongitude(coordinates.longitude);

                        }

                        customer.setRoles(roles);
                        customerRepo.save(customer);
                        user = customer;
                        user.setUsername(signUpRequest.getSignupRequestPatient().getUsername());
                        user.setEmail(signUpRequest.getSignupRequestPatient().getEmail());
                        user.setPassword(passwordEncoder.encode(signUpRequest.getSignupRequestPatient().getPassword()));
                        break;
                    default:
                        throw new RuntimeException("Error: Role is not found.");
                }
            }

            // Save user data in the User table

            userRepository.save(user);

            return user;
        }


    }
//    private boolean isPatientDataNotEmpty(SignupRequestPatient patient) {
//        return !StringUtils.isBlank(patient.getUsername()) && !StringUtils.isBlank(patient.getEmail());
//    }
//
//    private boolean isPharmacistDataNotEmpty(SignupRequestPharmacist pharmacist) {
//        return !StringUtils.isBlank(pharmacist.getUsername()) && !StringUtils.isBlank(pharmacist.getEmail());
//    }
private boolean isPatientDataValid(SignupRequest signUpRequest) {
    SignupRequestPatient patient = signUpRequest.getSignupRequestPatient();
    return patient != null && isPatientDataNotEmpty(patient);
}
    private boolean isPharmacistDataValid(SignupRequest signUpRequest) {
        SignupRequestPharmacist pharmacist = signUpRequest.getSignupRequestPharmacist();
        return pharmacist != null && isPharmacistDataNotEmpty(pharmacist);
    }

    // Implement these methods based on your specific validation requirements
    private boolean isPatientDataNotEmpty(SignupRequestPatient patient) {
        return patient.getUsername() != null && !patient.getUsername().isEmpty() && !patient.getUsername().equals("string")
                && patient.getEmail() != null && !patient.getEmail().isEmpty() && !patient.getEmail().equals("string")
                && patient.getPassword() != null && !patient.getPassword().isEmpty() && !patient.getPassword().equals("string")
                // Add other necessary checks if any
                ;
    }

    private boolean isPharmacistDataNotEmpty(SignupRequestPharmacist pharmacist) {
        return pharmacist.getUsername() != null && !pharmacist.getUsername().isEmpty() && !pharmacist.getUsername().equals("string")
                && pharmacist.getEmail() != null && !pharmacist.getEmail().isEmpty() && !pharmacist.getEmail().equals("string")
                && pharmacist.getPassword() != null && !pharmacist.getPassword().isEmpty() && !pharmacist.getPassword().equals("string")
                // Add other necessary checks if any
                ;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDTO> findDistinctChatUsers(Long userId) {
        return null;
    }


}
