package com.geocodinglocationservices.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Patient name is required")
    private String patientName;

    @NotEmpty(message = "Contact email is required")
    private String contactEmail;

    @NotEmpty(message = "Medication name is required")
    private String medicationName;

    @NotEmpty(message = "Medication dosage is required")
    private String medicationDosage;

    @NotEmpty(message = "Medication strength is required")
    private String medicationStrength;

    @NotNull(message = "Medication quantity is required")
    private int medicationQuantity;

    private String additionalNotes;

    @NotNull(message = "Request time is required")
    private Timestamp requestTime;

    @NotNull(message = "Request date is required")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
}
