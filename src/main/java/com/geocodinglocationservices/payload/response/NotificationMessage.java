package com.geocodinglocationservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String message;
    private String medicationName;
    private LocalDateTime reminderTime;
}
