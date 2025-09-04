package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.CheckoutRequestDTO;
import com.anubhav.ecom.dtos.CheckoutResponseDTO;
import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.exceptions.PaymentGatewayException;
import com.anubhav.ecom.models.*;
import com.anubhav.ecom.repositories.CartRepository;
import com.anubhav.ecom.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService
{
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;


    @Override
    @Transactional
    public CheckoutResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO)
    {
        Cart cart = cartRepository.getCartWithItems(checkoutRequestDTO.getCartId()).orElseThrow(()-> new BadRequestException("Invalid cart id : " + checkoutRequestDTO.getCartId()));
        if(cart.getCartItems().isEmpty())
        {
            throw new BadRequestException("Cart is empty");
        }

        User user = userService.getCurrentUser();

        var order = Order.fromCart(cart, user);

        orderRepository.save(order);

        try
        {
            //create a checkout session
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponseDTO(order.getId(), session.getCheckoutUrl());
        }
        catch (PaymentGatewayException ex)
        {
            orderRepository.delete(order);
            throw ex;
        }

    }

    @Override
    public void webhook(String signature, String payload)
    {
        paymentGateway.parseWebhookEvent(payload, signature)
        .ifPresent(result ->
        {
           var order = orderRepository.findById(result.getOrderId()).orElseThrow(()-> new BadRequestException("Invalid order id : " + result.getOrderId()));
           order.setStatus(OrderStatus.valueOf(result.getStatus().name()));
           orderRepository.save(order);
        });
    }
}
