package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.MedicineRequestService;
import com.geocodinglocationservices.models.Medicine;
import com.geocodinglocationservices.payload.request.MedicineRequest;
import com.geocodinglocationservices.repository.MedicineRequestRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineRequestImpl  implements MedicineRequestService {
    @Autowired
    private MedicineRequestRepo medicineRequestRepo;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public MedicineRequest sendMedicineRequest(MedicineRequest request) {

        Medicine medicineRequest = modelMapper.map(request,Medicine.class);
        Medicine save = medicineRequestRepo.save(medicineRequest);

        return modelMapper.map(save,MedicineRequest.class);
    }
}
