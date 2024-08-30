package com.geocodinglocationservices.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationMessage {
    private String message;
    private String medicationName;
    private LocalDateTime reminderTime;
}
