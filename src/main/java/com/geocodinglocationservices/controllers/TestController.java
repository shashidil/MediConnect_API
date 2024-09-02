package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.payload.response.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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


    @Autowired
    private NotificationController notificationController;

    // Endpoint to trigger a test notification
//    @GetMapping("/sendTestNotification")
//    public String sendTestNotification(@RequestParam String userId) {
//        // Create a sample message
//        NotificationMessage message = new NotificationMessage(
//                "This is a test reminder message!",
//                "Sample Medication",
//                LocalDateTime.now().plusMinutes(15)
//        );
//
//        // Send the message to the specified user
//        notificationController.sendMedicationReminder(userId, message);
//
//        return "Test notification sent to user with ID: " + userId;
//    }
}
