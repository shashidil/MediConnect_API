package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.response.OrderResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface OrderService {

    OrderResponse processPayment(String invoiceId,String orderNumber, Long pharmacistId, Long customerId, String paymentMethod, Double amount);

    PaymentIntent createPaymentIntent(int amount) throws StripeException;
}
