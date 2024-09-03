package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.payload.response.NotificationMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private FirebaseMessaging firebaseMessaging;

    public void sendMedicationReminder(String fcmToken, NotificationMessage message) {
        System.out.println("Sending notification to FCM token: " + fcmToken);
        System.out.println("Notification Message: " + message.toString());

        try {
            // Build the notification message
            Message firebaseMessage = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle("Medication Reminder")
                            .setBody(message.getMessage())
                            .build())
                    .putData("reminderTime", message.getReminderTime().toString())
                    .build();

            // Send the notification via Firebase Messaging
            String response = firebaseMessaging.send(firebaseMessage);
            System.out.println("Successfully sent notification: " + response);
        } catch (Exception e) {
            System.err.println("Error sending FCM notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void sendMedicationReminder(String userId, NotificationMessage message) {
//        System.out.println("Sending notification to user: " + userId);
//        System.out.println("Notification Message: " + message.toString());
//        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", message);
//    }


}
