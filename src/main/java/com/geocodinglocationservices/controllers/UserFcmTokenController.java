package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.UserFcmTokenService;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.FCMTokenRequest;
import com.geocodinglocationservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/userfcmtoken")
public class UserFcmTokenController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> saveFCMToken(@RequestBody FCMTokenRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeviceToken(request.getToken());
            userRepository.save(user);

            return ResponseEntity.ok("Token saved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
