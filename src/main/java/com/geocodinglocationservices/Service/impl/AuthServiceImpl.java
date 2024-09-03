package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.controllers.NotificationController;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.SignupRequest;
import com.geocodinglocationservices.payload.request.SignupRequestPatient;
import com.geocodinglocationservices.payload.request.SignupRequestPharmacist;
import com.geocodinglocationservices.payload.request.UserUpdateRequest;
import com.geocodinglocationservices.payload.response.NotificationMessage;
import com.geocodinglocationservices.payload.response.UserDTO;
import com.geocodinglocationservices.payload.response.UserDetailsDto;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.RoleRepository;
import com.geocodinglocationservices.repository.UserRepository;
import com.geocodinglocationservices.security.services.ReminderService;
import com.geocodinglocationservices.utill.GeocodingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDateTime;
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

    @Autowired
    ReminderService reminderService;
    @Autowired
    NotificationController notificationController;

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
            throw new RuntimeException("Error: Role 1 is not provided!");
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

                ;
    }

    private boolean isPharmacistDataNotEmpty(SignupRequestPharmacist pharmacist) {
        return pharmacist.getUsername() != null && !pharmacist.getUsername().isEmpty() && !pharmacist.getUsername().equals("string")
                && pharmacist.getEmail() != null && !pharmacist.getEmail().isEmpty() && !pharmacist.getEmail().equals("string")
                && pharmacist.getPassword() != null && !pharmacist.getPassword().isEmpty() && !pharmacist.getPassword().equals("string")

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

    @Override
    public UserDTO updateUser(Long userId, UserUpdateRequest updateRequest) throws IOException {
        String fullAddress;
        GeocodingService.LatLng coordinates;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        fullAddress = String.format("%s, %s, %s",
                updateRequest.getAddressLine1(),
                updateRequest.getCity(),
                updateRequest.getStates());
        coordinates = GeocodingService.getCoordinates(fullAddress);

        if (coordinates != null) {
            updateRequest.setLatitude(coordinates.latitude);
            updateRequest.setLongitude(coordinates.longitude);
        }
        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        if (updateRequest.getPassword() != null) {
            user.setPassword(updateRequest.getPassword());
        }
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            customer.setFirstName(updateRequest.getFirstName());
            customer.setLastName(updateRequest.getLastName());
            customer.setPhoneNumber(updateRequest.getPhoneNumber());
            customer.setAddressLine1(updateRequest.getAddressLine1());
            customer.setCity(updateRequest.getCity());
            customer.setStates(updateRequest.getStates());
            customer.setPostalCode(updateRequest.getPostalCode());
            customer.setLatitude(updateRequest.getLatitude());
            customer.setLongitude(updateRequest.getLongitude());

             customerRepo.save(customer);
        } else if (user instanceof Pharmacist) {
            Pharmacist pharmacist = (Pharmacist) user;
            pharmacist.setPharmacyName(updateRequest.getPharmacyName());
            pharmacist.setAddressLine1(updateRequest.getAddressLine1());
            pharmacist.setCity(updateRequest.getCity());
            pharmacist.setStates(updateRequest.getStates());
            pharmacist.setPostalCode(updateRequest.getPostalCode());
            pharmacist.setLatitude(updateRequest.getLatitude());
            pharmacist.setLongitude(updateRequest.getLongitude());

             pharmacistRepo.save(pharmacist);
        }

        User save = userRepository.save(user);
        UserDTO dto = new UserDTO();
        dto.setName(save.getUsername());
        dto.setId(save.getId());
        dto.setEmail(save.getEmail());
        return dto;
    }

    @Override
    public void handleNotificationInLogin(Long userId) {
        if (reminderService.shouldNotifyUser(userId)) {
            // Create and send the notification
            System.out.println(reminderService.shouldNotifyUser(userId));
            NotificationMessage message = new NotificationMessage();
            message.setMessage("It's time to reorder your medication!");
            message.setReminderTime(LocalDateTime.now());


            // Remove the user from the notification list
            reminderService.removeUserFromNotificationList(userId);
        }



    }

    @Override
    public UserDetailsDto getUserDetails(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setUsername(user.getUsername());
        userDetailsDto.setEmail(user.getEmail());



        if (user instanceof Customer) {
            // Populate Customer-specific fields
            Customer customer = (Customer) user;
            userDetailsDto.setFirstName(customer.getFirstName());
            userDetailsDto.setLastName(customer.getLastName());
            userDetailsDto.setPhoneNumber(customer.getPhoneNumber());
            userDetailsDto.setAddressLine1(customer.getAddressLine1());
            userDetailsDto.setCity(customer.getCity());
            userDetailsDto.setStates(customer.getStates());
            userDetailsDto.setPostalCode(customer.getPostalCode());
            userDetailsDto.setRole("Customer");
        } else if (user instanceof Pharmacist) {
            // Populate Pharmacist-specific fields
            Pharmacist pharmacist = (Pharmacist) user;
            userDetailsDto.setPharmacyName(pharmacist.getPharmacyName());
            userDetailsDto.setAddressLine1(pharmacist.getAddressLine1());
            userDetailsDto.setCity(pharmacist.getCity());
            userDetailsDto.setStates(pharmacist.getStates());
            userDetailsDto.setPostalCode(pharmacist.getPostalCode());
            userDetailsDto.setRole("Pharmacist");
        }

        return userDetailsDto;
    }


}




