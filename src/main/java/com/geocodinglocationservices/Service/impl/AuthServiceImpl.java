package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import com.geocodinglocationservices.utill.GeocodingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashSet;
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
        Set<String> strRoles = signUpRequest.getRole(); // Accept roles as a set of strings
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
                    case "pharmacist":
                        Role modRole = roleRepository.findByName(ERole.ROLE_PHARMACIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        Pharmacist pharmacist = modelMapper.map(signUpRequest, Pharmacist.class);

                        // Concatenate address components to form a full address
                        String fullAddress = String.format("%s, %s, %s",
                                pharmacist.getAddressLine1(),
                                pharmacist.getCity(),
                                pharmacist.getStates());
                                //pharmacist.getPostalCode());

                        // Use the full address for geocoding
                        GeocodingService.LatLng coordinates = GeocodingService.getCoordinates(fullAddress);
                        //NominatimGeocodingService.LatLng coordinates = NominatimGeocodingService.getCoordinates(fullAddress);

                        if (coordinates != null) {
                            pharmacist.setLatitude(coordinates.latitude);
                            pharmacist.setLongitude(coordinates.longitude);

                        }

                        pharmacist.setRoles(roles);
                        pharmacistRepo.save(pharmacist);
                        user = pharmacist;
                        break;
                    case "customer":
                        Role cusRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(cusRole);
                        Customer customer = modelMapper.map(signUpRequest, Customer.class);
                        customer.setRoles(roles);
                        customerRepo.save(customer);
                        user = customer;
                        break;
                    default:
                        throw new RuntimeException("Error: Role is not found.");
                }
            }

            // Save user data in the User table
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            userRepository.save(user);

            return user;
        }
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
