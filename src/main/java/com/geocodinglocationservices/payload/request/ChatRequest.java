package com.geocodinglocationservices.payload.request;

import com.geocodinglocationservices.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private String content;
    private Long senderId;
    private Long receiverId;
}
