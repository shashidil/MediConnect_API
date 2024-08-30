package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private AuthService authService;

//    @PostMapping("/trigger-reminder/{userId}")
//    public ResponseEntity<?> triggerReminderForUser(@PathVariable Long userId) {
//        // Simulate a user login and check if they should receive a notification
//        authService.handleNotificationInLogin(userId);
//
//        return ResponseEntity.ok("Reminder check completed for user ID: " + userId);
//    }
}
