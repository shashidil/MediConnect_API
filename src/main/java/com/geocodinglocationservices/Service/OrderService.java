package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.request.PaymentRequest;
import com.geocodinglocationservices.payload.request.UpdateOrderRequest;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.List;

public interface OrderService {

    OrderResponse processPayment(PaymentRequest paymentRequest);

    PaymentIntent createPaymentIntent(int amount) throws StripeException;

    List<OrderResponse> getOrders(Long customerId,Long pharmacistId);

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderRequest updateRequest);
}
