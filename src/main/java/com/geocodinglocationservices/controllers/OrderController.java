package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.OrderService;
import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.request.UpdateOrderRequest;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            @RequestParam Long Id,
            @RequestParam String orderNumber,
            @RequestParam Long pharmacistId,
            @RequestParam Long customerId,
            @RequestParam String paymentMethod,
            @RequestParam Double amount) {
        OrderResponse order = orderService.processPayment(Id,orderNumber, pharmacistId, customerId, paymentMethod, amount);
        return ResponseEntity.ok(order);
    }

    @GetMapping("history")
    public ResponseEntity<List<OrderResponse>> getOrders( @RequestParam(required = false) Long customerId,
                                                          @RequestParam(required = false) Long pharmacistId){

        List<OrderResponse> orders = orderService.getOrders(customerId,pharmacistId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderRequest updateRequest) {
        System.out.println(updateRequest.getTrackingNumber());
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, updateRequest);
        return ResponseEntity.ok(updatedOrder);
    }
}
