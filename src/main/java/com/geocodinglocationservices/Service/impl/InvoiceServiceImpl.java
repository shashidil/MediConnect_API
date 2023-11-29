package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.InvoiceService;
import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.InvoiceRepository;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.PrescriptionRepo;
import com.geocodinglocationservices.utill.GeocodingDistance;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PrescriptionRepo prescriptionRepo;

    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    PharmacistRepo pharmacistRepo;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public InvoiceRequest createInvoice(InvoiceRequest invoice) {
        Prescription prescriptionById = prescriptionRepo.findById(invoice.getPrescriptionId()).orElseThrow(() -> new EntityNotFoundException("Prescription not found"));
        MedicineInvoice medicineInvoice = modelMapper.map(invoice, MedicineInvoice.class);
        medicineInvoice.setPatientName(prescriptionById.getUser().getUsername());
        return modelMapper.map(invoiceRepository.save(medicineInvoice), InvoiceRequest.class);
    }

    @Override
    public InvoiceResponse getInvoiceForCustomer(Long invoiceId, Long customerId) throws Exception {

        MedicineInvoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        Prescription prescription = invoice.getPrescription();

        if (prescription.getUser().getId().equals(customerId)) {
            Long pharmacistId = invoice.getPharmacist().getId();
            List<Pharmacist> authorizedPharmacists = prescription.getPharmacists();

            boolean isPharmacistAuthorized = authorizedPharmacists.stream()
                    .anyMatch(pharmacist -> pharmacist.getId().equals(pharmacistId));

            Customer existCustomer = customerRepo.findById(customerId).orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
            Pharmacist existPharmacist = pharmacistRepo.findById(pharmacistId).orElseThrow(() -> new UsernameNotFoundException("Pharmacist not found"));
            double distance = GeocodingDistance.getDistance(existCustomer.getLatitude(), existCustomer.getLongitude(), existPharmacist.getLatitude(), existPharmacist.getLongitude());
            //distance add
            if (isPharmacistAuthorized) {
                return modelMapper.map(invoice, InvoiceResponse.class);
            } else {
                throw new AccessDeniedException("The pharmacist is not authorized to access this invoice.");
            }
        } else {
            throw new AccessDeniedException("This prescription does not belong to the customer.");
        }
    }
}
