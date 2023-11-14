package com.geocodinglocationservices.repository;

import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepo extends JpaRepository<Prescription,Long> {

    List<Prescription> findByUser(Optional<User> user);
}
