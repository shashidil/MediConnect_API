package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.PharmacistService;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.repository.PharmacistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhamacistSeriviceImpl implements PharmacistService {
    @Autowired
    PharmacistRepo pharmacistRepo;

    @Override
    public List<Pharmacist> getPharmacistsByCity(String city) {
        return pharmacistRepo.findByCity(city);
    }

    @Override
    public List<Pharmacist> getPharmacistsByState(String state) {
        return pharmacistRepo.findByStates(state);
    }

    @Override
    public Pharmacist getPharmacistById(Long id) {
        Optional<Pharmacist> pharmacist = pharmacistRepo.findById(id);
        if (pharmacist.isPresent()) {
            return pharmacist.get();
        } else {
            throw new RuntimeException("Pharmacist not found with id: " + id);
        }
    }
}
