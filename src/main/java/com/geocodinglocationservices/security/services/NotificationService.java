package com.geocodinglocationservices.security.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public String sendReorderNotification(String token, String medicationName) {
        Notification notification = Notification.builder()
                .setTitle("Medication Reorder Reminder")
                .setBody("It's time to reorder your medication: " + medicationName)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return "Successfully sent message: " + response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send notification";
        }
    }
}
