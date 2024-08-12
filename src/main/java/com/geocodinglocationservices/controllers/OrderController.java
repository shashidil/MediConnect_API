package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.OrderService;
import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    OrderService orderService;


    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntent> createPaymentIntent(@RequestBody Map<String, Integer> data) throws StripeException {
        int amount = data.get("amount");
        PaymentIntent paymentIntent = orderService.createPaymentIntent(amount);
        return ResponseEntity.ok(paymentIntent);
    }

    @PostMapping("/process-payment")
    public ResponseEntity<OrderResponse> processPayment(
            @RequestParam String invoiceId,
            @RequestParam String orderNumber,
            @RequestParam Long pharmacistId,
            @RequestParam Long customerId,
            @RequestParam String paymentMethod,
            @RequestParam Double amount) {
        OrderResponse order = orderService.processPayment(invoiceId,orderNumber, pharmacistId, customerId, paymentMethod, amount);
        return ResponseEntity.ok(order);
    }
}
