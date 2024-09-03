package com.geocodinglocationservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private UserDTO user;
    private String medicationName;
    private int medicationQuantity;
    private byte[] imageData;
}
