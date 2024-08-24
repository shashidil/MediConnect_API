package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.InvoiceService;
import com.geocodinglocationservices.models.Customer;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.Prescription;
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

    private void configureModelMapper() {
        modelMapper.addMappings(new PropertyMap<InvoiceRequest, MedicineInvoice>() {
            @Override
            protected void configure() {
                // Explicitly map properties
                map().setMedicationName(source.getMedications().get(0).getMedicationName());
                map().setMedicationDosage(source.getMedications().get(1).getMedicationDosage());
                map().setMedicationQuantity(source.getMedications().get(2).getMedicationQuantity());
                map().setAmount(source.getMedications().get(3).getAmount());
               // map().setAdditionalNotes(source.getMedications().get().get);
                // Ignore the ID mapping
                skip(destination.getId());
            }
        });
    }

    @Override
    public InvoiceRequest createInvoice(InvoiceRequest invoice) {
        Prescription prescriptionById = prescriptionRepo.findById(invoice.getPrescriptionId())
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found"));
        Pharmacist pharmacist = pharmacistRepo.findById(invoice.getPharmacistId())
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist Not Found"));
        Customer customer = customerRepo.findById(prescriptionById.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found"));

        // Iterate over each medication and create an invoice entry
        for (InvoiceRequest.Medication medication : invoice.getMedications()) {
            MedicineInvoice medicineInvoice = new MedicineInvoice();
            medicineInvoice.setPrescription(prescriptionById);
            medicineInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
            medicineInvoice.setPharmacist(pharmacist);
            medicineInvoice.setCustomer(customer);
            medicineInvoice.setPatientName(prescriptionById.getUser().getUsername());
            medicineInvoice.setMedicationName(medication.getMedicationName());
            medicineInvoice.setMedicationDosage(medication.getMedicationDosage());
            medicineInvoice.setMedicationQuantity(medication.getMedicationQuantity());
            medicineInvoice.setAmount(medication.getAmount());
            medicineInvoice.setTotal(invoice.getTotalAmount());
            // Save each MedicineInvoice
            invoiceRepository.save(medicineInvoice);
        }

        return invoice;
    }

    @Override
    public List<InvoiceResponse> getInvoiceForCustomer(Long customerId) throws Exception {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        List<MedicineInvoice> invoices = invoiceRepository.findByCustomer(customer);
        Map<Long, InvoiceResponse> invoiceMap = new HashMap<>();

        for (MedicineInvoice invoice : invoices) {
            Long prescriptionId = invoice.getPrescription().getId();
            InvoiceResponse invoiceResponse = invoiceMap.get(prescriptionId);

            if (invoiceResponse == null) {
                invoiceResponse = new InvoiceResponse();
                invoiceResponse.setPharmacistName(invoice.getPharmacist().getPharmacyName());
                invoiceResponse.setAdditionalNotes(invoice.getAdditionalNotes());
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
                invoiceResponse.setMedications(new ArrayList<>());

                invoiceMap.put(prescriptionId, invoiceResponse);
            }

            MedicationResponse medicationResponse = new MedicationResponse();
            medicationResponse.setMedicationName(invoice.getMedicationName());
            medicationResponse.setMedicationDosage(invoice.getMedicationDosage());
            medicationResponse.setMedicationQuantity(invoice.getMedicationQuantity());
            medicationResponse.setAmount(invoice.getAmount());


            invoiceResponse.getMedications().add(medicationResponse);
        }

        return new ArrayList<>(invoiceMap.values());
    }

    @Override
    public List<InvoiceResponse> getInvoicesByInvoiceNumber(String invoiceNumber) {
        // Fetch all MedicineInvoices with the given invoiceNumber
        List<MedicineInvoice> medicineInvoices = invoiceRepository.findByinvoiceNumber(invoiceNumber);

        // Create a map to aggregate the results
        Map<String, InvoiceResponse> invoiceMap = new HashMap<>();

        for (MedicineInvoice invoice : medicineInvoices) {
            // Create or update the InvoiceResponse in the map
            InvoiceResponse invoiceResponse = invoiceMap.computeIfAbsent(invoice.getInvoiceNumber(), key -> {
                InvoiceResponse response = new InvoiceResponse();
                response.setPharmacistName(invoice.getPharmacist().getPharmacyName()); // Assuming you have a method to get the name
                response.setInvoiceNumber(invoice.getInvoiceNumber());
                response.setTotal(invoice.getTotal());
                response.setAdditionalNotes(invoice.getAdditionalNotes());
                response.setDistance(0.0); // Or calculate the distance if needed
                response.setPharmacistId(invoice.getPharmacist().getId());
                response.setPharmacistLatitude(invoice.getPharmacist().getLatitude());
                response.setPharmacistLongitude(invoice.getPharmacist().getLongitude());
                response.setCustomerLatitude(invoice.getCustomer().getLatitude());
                response.setCustomerLongitude(invoice.getCustomer().getLongitude());
                return response;
            });

            // Add medication to the list
            MedicationResponse medicationResponse = new MedicationResponse();
            medicationResponse.setMedicationName(invoice.getMedicationName());
            medicationResponse.setMedicationDosage(invoice.getMedicationDosage());
            medicationResponse.setMedicationQuantity(invoice.getMedicationQuantity());
            medicationResponse.setAmount(invoice.getAmount());

            // Initialize the medications list if it doesn't exist
            if (invoiceResponse.getMedications() == null) {
                invoiceResponse.setMedications(new ArrayList<>());
            }
            invoiceResponse.getMedications().add(medicationResponse);
        }

        // Return the combined list
        return new ArrayList<>(invoiceMap.values());
    }
}
