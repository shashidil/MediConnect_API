package com.geocodinglocationservices.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCMTokenRequest {
    private String token;
    private Long userId;
}
