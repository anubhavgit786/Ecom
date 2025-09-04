package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.CheckoutSession;
import com.anubhav.ecom.dtos.PaymentResult;
import com.anubhav.ecom.exceptions.PaymentGatewayException;
import com.anubhav.ecom.models.Order;
import java.util.Optional;

public interface PaymentGateway
{
    CheckoutSession createCheckoutSession(Order order) throws PaymentGatewayException;
    Optional<PaymentResult> parseWebhookEvent(String payload, String signature);
}
