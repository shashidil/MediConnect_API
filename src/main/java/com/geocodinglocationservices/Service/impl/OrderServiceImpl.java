package com.geocodinglocationservices.Service.impl;

import com.amazonaws.services.workmail.model.EntityNotFoundException;
import com.geocodinglocationservices.Service.OrderService;
import com.geocodinglocationservices.models.*;
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

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public OrderResponse processPayment(String invoiceId, String orderNumber,Long pharmacistId, Long customerId, String paymentMethod, Double amount) {
        Stripe.apiKey = apiKey;

        List<MedicineInvoice> invoice = invoiceRepository.findAllByinvoiceNumber(invoiceId);
        if (invoice.isEmpty()) throw new EntityNotFoundException("Invoice not found");
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        Pharmacist pharmacist = pharmacistRepo.findById(pharmacistId)
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist not found"));

        /*PharmacistAccount pharmacistAccount = pharmacistAccountRepo.findByPharmacistId(pharmacistId)
                .orElseThrow(() -> new UsernameNotFoundException("Pharmacist stripe account not found"));*/

        // Create and save the Order before processing payment
        Order order = new Order();
        order.setInvoiceNumber(invoiceId);
        order.setOrderNumber(orderNumber);
        order.setCustomer(customer);
        order.setPharmacist(pharmacist);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("Pending");
        order.setOrderStatus("Awaiting Shipment"); // Default status
        order.setTotalAmount(amount);


        Map<String, Object> paymentIntentParams = new HashMap<>();
        paymentIntentParams.put("amount", (int) (amount * 100)); // Convert amount to cents
        paymentIntentParams.put("currency", "usd");
        paymentIntentParams.put("payment_method_types", new String[]{"card"});
   /*     paymentIntentParams.put("transfer_data", Map.of(
                "destination", pharmacistAccount.getStripeAccountId()
        ));*/

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);
            order.setPaymentIntentId(paymentIntent.getId());
            order.setPaymentStatus("Completed");
            orderRepo.save(order);
            return modelMapper.map(order,OrderResponse.class);

        } catch (StripeException e) {
            throw new RuntimeException("Failed to process payment", e);
        }
    }

    @Override
    public PaymentIntent createPaymentIntent(int amount) {
        Stripe.apiKey = apiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");

        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            System.out.println("StripeException: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stripe API error: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle other potential exceptions
            System.out.println("Exception: " + e.getMessage());

            // Respond with a more generic error status code
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error occurred", e);
        }

    }

    @Override
    public List<OrderResponse> getOrders(Long customerId,Long pharmacistId) {
        List<Order> byCustomerId = new ArrayList<>();
        if (customerId != null) {
            Customer customer = customerRepo.findById(customerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
            byCustomerId = orderRepo.findByCustomerId(customerId);
        }else if (pharmacistId != null) {
            Pharmacist pharmacist= pharmacistRepo.findById(pharmacistId)
                    .orElseThrow(() -> new UsernameNotFoundException("Pharmacist not found"));
            byCustomerId= orderRepo.findByPharmacistId(pharmacistId);
        } else {
            throw new IllegalArgumentException("Either customerId or pharmacistId must be provided");
        }
        return modelMapper.map(byCustomerId, new TypeToken<List<OrderResponse>>(){}.getType());
    }

    @Override
    public OrderResponse updateOrderStatus(String orderId, UpdateOrderRequest updateRequest) {
        Order order = orderRepo.findByorderNumber(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (updateRequest.getStatus() != null && !updateRequest.getStatus().isEmpty()){
            order.setOrderStatus(updateRequest.getStatus());
        }
        else if(updateRequest.getTrackingNumber() != null &&  !updateRequest.getTrackingNumber().isEmpty()){
            order.setTrackingNumber(updateRequest.getTrackingNumber());
        }

        orderRepo.save(order);

        return modelMapper.map(order, OrderResponse.class);
    }


}


