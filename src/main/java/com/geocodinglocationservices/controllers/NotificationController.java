package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.payload.response.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMedicationReminder(String userId, NotificationMessage message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", message);
    }


}
