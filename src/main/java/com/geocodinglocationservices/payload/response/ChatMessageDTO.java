package com.geocodinglocationservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private String content;
    private UserDTO sender;
    private UserDTO receiver;
    private Timestamp timestamp;
}
