package com.geocodinglocationservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponseDTO {
    private Long id;
    private String subject;
    private String message;
    private String status;
    private Long senderId;
    private Timestamp createdDate;
    private Timestamp lastUpdated;
}
