package com.geocodinglocationservices.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MedicationDetail {
    private String medicationName;
    private String medicationDosage;
    private String days;
    private int medicationQuantity;
    private Double amount;
}
