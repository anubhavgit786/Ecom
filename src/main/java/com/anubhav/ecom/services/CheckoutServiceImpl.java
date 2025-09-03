package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.CheckoutRequestDTO;
import com.anubhav.ecom.dtos.CheckoutResponseDTO;
import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.models.*;
import com.anubhav.ecom.repositories.CartRepository;
import com.anubhav.ecom.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService
{
    private final CartRepository cartRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
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
        cartService.clearCart(cart.getId());

        return new CheckoutResponseDTO(order.getId());

    }
}
