package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    private ModelMapper modelMapper = new ModelMapper();
//    @Override
//    public User registerUser(SignupRequest signUpRequest) {
//       String strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//        User user = new User();
//
//        if (strRoles == null) {
////            Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
////                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
////            roles.add(userRole);
//           throw  new RuntimeException("Error: Role is not found.");
//        } else {
//                switch (strRoles) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                    case "pharmacist":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_PHARMACIST)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//                        Pharmacist pharmacist = modelMapper.map(signUpRequest, Pharmacist.class);
//                        pharmacist.setRole(String.valueOf(roles));
//                        pharmacistRepo.save(pharmacist);
//                        user.setUsername(pharmacist.getUsername());
//                        user.setEmail(pharmacist.getEmail());
//                        user.setPassword(pharmacist.getPassword());
//                        user.setRoles(roles);
//                        return userRepository.save(user);
//
//
//                    case "customer":
//                        Role cusRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(cusRole);
//                        Customer customer = modelMapper.map(signUpRequest, Customer.class);
//                        customer.setRole(String.valueOf(roles));
//                        customerRepo.save(customer);
//                        user.setUsername(customer.getUsername());
//                        user.setEmail(customer.getEmail());
//                        user.setPassword(customer.getPassword());
//                        user.setRoles(roles);
//                       return userRepository.save(user);
////                    default:
////                        Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
////                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
////                        roles.add(userRole);
////                        User user = modelMapper.map(signUpRequest,User.class);
////                        userRepository.save(user);
//                }
//
//        }
//
//        User defaultUser = modelMapper.map(signUpRequest, User.class);
//                return userRepository.save(defaultUser);
//    }

    public User registerUser(SignupRequest signUpRequest) {
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
            user.setPassword(signUpRequest.getPassword());
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
