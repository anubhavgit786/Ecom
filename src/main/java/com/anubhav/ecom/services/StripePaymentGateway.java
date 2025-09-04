package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.CheckoutSession;
import com.anubhav.ecom.dtos.PaymentResult;
import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.exceptions.PaymentGatewayException;
import com.anubhav.ecom.models.Order;
import com.anubhav.ecom.models.OrderItem;
import com.anubhav.ecom.models.OrderStatus;
import com.anubhav.ecom.models.PaymentStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway
{
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) throws PaymentGatewayException
    {
        try
        {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString());

            order.getItems().forEach(item ->
            {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }
        catch (StripeException ex)
        {
            throw new PaymentGatewayException("Stripe", ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookEvent(String payload, String signature)
    {
        try
        {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);


            switch (event.getType())
            {
                case "payment_intent.succeeded" ->
                {
                    return Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
                }

                case "payment_intent.payment_failed" ->
                {
                    return Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
                }

                default ->
                {
                    return Optional.empty();
                }
            }

        }
        catch (SignatureVerificationException ex)
        {
            throw new BadRequestException(ex.getMessage());
        }
    }

    private Long extractOrderId(Event event)
    {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(()-> new PaymentGatewayException("Could not deserialize stripe event. Check the SDK and API version"));
        var paymentIntent = (PaymentIntent) stripeObject;
        var id = paymentIntent.getMetadata().get("order_id");
        return Long.valueOf(id);
    }

    private ProductData createProductData(OrderItem item)
    {
        return ProductData.builder()
                .setName(item.getProduct().getName())
                .setDescription(item.getProduct().getDescription())
                .build();
    }

    private PriceData createPriceData(OrderItem item)
    {
        return PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount(item.getUnitPrice().multiply(new BigDecimal(100)).longValue())
                .setProductData(createProductData(item))
                .build();
    }

    private LineItem createLineItem(OrderItem item)
    {
        return LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }
}
