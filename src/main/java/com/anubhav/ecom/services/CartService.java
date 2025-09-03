package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.AddItemToCartRequestDTO;
import com.anubhav.ecom.dtos.CartDTO;
import com.anubhav.ecom.dtos.CartItemDTO;
import com.anubhav.ecom.dtos.UpdateCartItemRequestDTO;

import java.util.UUID;

public interface CartService
{
    CartDTO createCart();
    CartItemDTO addItemToCart(UUID cartId, AddItemToCartRequestDTO item);
    CartDTO getCart(UUID cartId);
    CartItemDTO updateItem(UUID cartId, Long productId, UpdateCartItemRequestDTO request);
    void removeItem(UUID cartId, Long productId);
    void clearCart(UUID cartId);
}
