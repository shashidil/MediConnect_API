package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.InvoiceService;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.InvoiceRequest;
import com.geocodinglocationservices.payload.response.InvoiceResponse;
import com.geocodinglocationservices.payload.response.MedicationResponse;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.geocodinglocationservices.repository.CustomerRepo;
import com.geocodinglocationservices.repository.InvoiceRepository;
import com.geocodinglocationservices.repository.PharmacistRepo;
import com.geocodinglocationservices.repository.PrescriptionRepo;
import com.geocodinglocationservices.utill.GeocodingDistance;
import com.stripe.model.Invoice;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public InvoiceServiceImpl() {
      //  configureModelMapper();
    }

//    private void configureModelMapper() {
//        modelMapper.addMappings(new PropertyMap<InvoiceRequest, MedicineInvoice>() {
//            @Override
//            protected void configure() {
//                // Explicitly map properties
//                map().setMedicationName(source.getMedications().get(0).getMedicationName());
//                map().setMedicationDosage(source.getMedications().get(1).getMedicationDosage());
//                map().setMedicationQuantity(source.getMedications().get(2).getMedicationQuantity());
//              //  map().setAmount(source.getMedications().get(3).getAmount());
//               // map().setAdditionalNotes(source.getMedications().get().get);
//                // Ignore the ID mapping
//                skip(destination.getId());
//            }
//        });
//    }

    @Override
    public InvoiceRequest createInvoice(InvoiceRequest invoice) {
        Prescription prescriptionById = prescriptionRepo.findById(invoice.getPrescriptionId())
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found"));
        Pharmacist pharmacist = pharmacistRepo.findById(invoice.getPharmacistId())
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist Not Found"));
        Customer customer = customerRepo.findById(prescriptionById.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found"));

        // Create a new MedicineInvoice
        MedicineInvoice medicineInvoice = new MedicineInvoice();
        medicineInvoice.setPrescription(prescriptionById);
        medicineInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        medicineInvoice.setPharmacist(pharmacist);
        medicineInvoice.setCustomer(customer);
        medicineInvoice.setPatientName(prescriptionById.getUser().getUsername());
        medicineInvoice.setTotal(invoice.getTotalAmount());

        List<MedicationDetail> medicationDetails = invoice.getMedications().stream().map(medication -> {
            MedicationDetail medicationDetail = new MedicationDetail();
            medicationDetail.setMedicationName(medication.getMedicationName());
            medicationDetail.setMedicationDosage(medication.getMedicationDosage());
            medicationDetail.setMedicationQuantity(medication.getMedicationQuantity());
            medicationDetail.setAmount(medication.getAmount());
            return medicationDetail;
        }).collect(Collectors.toList());

        medicineInvoice.setMedications(medicationDetails);

        // Save the MedicineInvoice with all medications
        invoiceRepository.save(medicineInvoice);

        return invoice;
    }

    @Override
    public List<InvoiceResponse> getInvoiceForCustomer(Long customerId) throws Exception {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        List<MedicineInvoice> invoices = invoiceRepository.findByCustomer(customer);
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        for (MedicineInvoice invoice : invoices) {
            InvoiceResponse invoiceResponse = new InvoiceResponse();
            invoiceResponse.setId(invoice.getId());
            invoiceResponse.setPharmacistName(invoice.getPharmacist().getPharmacyName());
            invoiceResponse.setDistance(GeocodingDistance.getDistance(
                    customer.getLatitude(), customer.getLongitude(),
                    invoice.getPharmacist().getLatitude(), invoice.getPharmacist().getLongitude()));
            invoiceResponse.setInvoiceNumber(invoice.getInvoiceNumber());
            invoiceResponse.setTotal(invoice.getTotal());
            invoiceResponse.setPharmacistId(invoice.getPharmacist().getId());
            invoiceResponse.setCustomerLatitude(customer.getLatitude());
            invoiceResponse.setCustomerLongitude(customer.getLongitude());
            invoiceResponse.setPharmacistLatitude(invoice.getPharmacist().getLatitude());
            invoiceResponse.setPharmacistLongitude(invoice.getPharmacist().getLongitude());

            // Convert the list of MedicationDetail to MedicationResponse
            List<MedicationResponse> medicationResponses = invoice.getMedications().stream().map(medication -> {
                MedicationResponse response = new MedicationResponse();
                response.setMedicationName(medication.getMedicationName());
                response.setMedicationDosage(medication.getMedicationDosage());
                response.setMedicationQuantity(medication.getMedicationQuantity());
                response.setAmount(medication.getAmount());
                return response;
            }).collect(Collectors.toList());

            invoiceResponse.setMedications(medicationResponses);

            invoiceResponses.add(invoiceResponse);
        }

        return invoiceResponses;
    }

    @Override
    public List<InvoiceResponse> getInvoicesByInvoiceNumber(String invoiceNumber) {
        List<MedicineInvoice> medicineInvoices = invoiceRepository.findByinvoiceNumber(invoiceNumber);
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        for (MedicineInvoice invoice : medicineInvoices) {
            InvoiceResponse invoiceResponse = new InvoiceResponse();
            invoiceResponse.setPharmacistName(invoice.getPharmacist().getPharmacyName());
            invoiceResponse.setInvoiceNumber(invoice.getInvoiceNumber());
            invoiceResponse.setTotal(invoice.getTotal());
            invoiceResponse.setDistance(0.0);  // Calculate distance if needed
            invoiceResponse.setPharmacistId(invoice.getPharmacist().getId());
            invoiceResponse.setPharmacistLatitude(invoice.getPharmacist().getLatitude());
            invoiceResponse.setPharmacistLongitude(invoice.getPharmacist().getLongitude());
            invoiceResponse.setCustomerLatitude(invoice.getCustomer().getLatitude());
            invoiceResponse.setCustomerLongitude(invoice.getCustomer().getLongitude());

            // Convert the list of MedicationDetail to MedicationResponse
            List<MedicationResponse> medicationResponses = invoice.getMedications().stream().map(medication -> {
                MedicationResponse response = new MedicationResponse();
                response.setMedicationName(medication.getMedicationName());
                response.setMedicationDosage(medication.getMedicationDosage());
                response.setMedicationQuantity(medication.getMedicationQuantity());
                response.setAmount(medication.getAmount());
                return response;
            }).collect(Collectors.toList());

            invoiceResponse.setMedications(medicationResponses);

            invoiceResponses.add(invoiceResponse);
        }

        return invoiceResponses;
    }
}
