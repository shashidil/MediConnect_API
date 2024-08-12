package com.geocodinglocationservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    @CreationTimestamp
    private Timestamp CreationTime;
    private String medicationName;
    private String medicationDosage;
    private int medicationQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(
            name = "prescription_pharmacist",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "pharmacist_id")
    )
    private List<Pharmacist> pharmacists = new ArrayList<>();

}
