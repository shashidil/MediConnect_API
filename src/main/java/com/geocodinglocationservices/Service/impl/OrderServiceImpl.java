package com.geocodinglocationservices.Service.impl;

import com.amazonaws.services.workmail.model.EntityNotFoundException;
import com.geocodinglocationservices.Service.OrderService;
import com.geocodinglocationservices.models.*;
import com.geocodinglocationservices.payload.request.PaymentRequest;
import com.geocodinglocationservices.payload.request.UpdateOrderRequest;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.geocodinglocationservices.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${stripe.api.key}")
    private String apiKey;

    @Autowired
    private PharmacistAccountRepo pharmacistAccountRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private PharmacistRepo pharmacistRepo;

    @Autowired
    private PrescriptionRepo prescriptionRepo;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public OrderResponse processPayment(PaymentRequest paymentRequest) {

        MedicineInvoice invoice = invoiceRepository.findById(paymentRequest.getInvoiceId())
                .orElseThrow(() -> new UsernameNotFoundException("Invoice not found"));
        Customer customer = customerRepo.findById(paymentRequest.getCustomerId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        Pharmacist pharmacist = pharmacistRepo.findById(paymentRequest.getPharmacistId())
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist not found"));

        /*PharmacistAccount pharmacistAccount = pharmacistAccountRepo.findByPharmacistId(pharmacistId)
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist stripe account not found"));*/

        Order order = new Order();
        order.setInvoiceNumber(invoice.getInvoiceNumber());
        order.setOrderNumber(paymentRequest.getOrderNumber());
        order.setCustomer(customer);
        order.setPharmacist(pharmacist);
        order.setInvoice(invoice);
        order.setPaymentMethod(paymentRequest.getPaymentMethod());
        order.setPaymentStatus("Pending");
        order.setOrderStatus("Awaiting Shipment");
        order.setTotalAmount(paymentRequest.getAmount());
        order.setPaymentStatus("Completed");

        setStatus(invoice.getPrescription().getId());

        orderRepo.save(order);
        return modelMapper.map(order,OrderResponse.class);

    }

    @Override
    public PaymentIntent createPaymentIntent(int amount) {
        Stripe.apiKey = apiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");

        try {
            return PaymentIntent.create(params); // Keep returning PaymentIntent
        } catch (StripeException e) {
            System.out.println("StripeException: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stripe API error: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error occurred", e);
        }
    }


    @Override
    public List<OrderResponse> getOrders(Long customerId,Long pharmacistId) {
        List<Order> orderById = new ArrayList<>();
        if (customerId != null) {
            Customer customer = customerRepo.findById(customerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
            orderById = orderRepo.findByCustomerId(customerId);
        }else if (pharmacistId != null) {
            Pharmacist pharmacist= pharmacistRepo.findById(pharmacistId)
                    .orElseThrow(() -> new UsernameNotFoundException("Pharmacist not found"));
            orderById= orderRepo.findByPharmacistId(pharmacistId);
        } else {
            throw new IllegalArgumentException("Either customerId or pharmacistId must be provided");
        }


        return modelMapper.map(orderById, new TypeToken<List<OrderResponse>>(){}.getType());
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderRequest updateRequest) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (updateRequest.getStatus() != null && !updateRequest.getStatus().isEmpty()){
            order.setOrderStatus(updateRequest.getStatus());
        }
        if(updateRequest.getTrackingNumber() != null &&  !updateRequest.getTrackingNumber().isEmpty()){
            order.setTrackingNumber(updateRequest.getTrackingNumber());
        }

        orderRepo.save(order);

        return modelMapper.map(order, OrderResponse.class);
    }

    public void setStatus(Long prescriptionId){

        Prescription prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(() -> new RuntimeException("PrescriptionNot Found"));
        List<MedicineInvoice> medicineInvoiceList = invoiceRepository.findByPrescription(prescription);

        for (MedicineInvoice invoice : medicineInvoiceList) {
            invoice.setStatus("Complete");
            invoiceRepository.save(invoice);
        }

    }


}


