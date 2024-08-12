package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Pharmacist;

import java.util.List;

public interface PharmacistService {
    List<Pharmacist> getPharmacistsByCity(String city);

    List<Pharmacist> getPharmacistsByState(String state);

    Pharmacist getPharmacistById(Long id);
}
