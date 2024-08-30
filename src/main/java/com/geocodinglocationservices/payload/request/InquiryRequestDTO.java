package com.geocodinglocationservices.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequestDTO {
    private Long id;
    private String subject;
    private String message;
    private String status;
    private Long senderId;
    private Timestamp createdDate;
    private Timestamp lastUpdated;
}