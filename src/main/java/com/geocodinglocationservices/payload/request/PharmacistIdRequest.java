package com.geocodinglocationservices.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PharmacistIdRequest {

    @NotNull(message = "Pharmacist IDs list cannot be null")
    @NotEmpty(message = "Pharmacist IDs list cannot be empty")
    private List<Long> pharmacistId;
}
