package com.anubhav.ecom.controllers;

import com.anubhav.ecom.dtos.AddItemToCartRequestDTO;
import com.anubhav.ecom.dtos.CartDTO;
import com.anubhav.ecom.dtos.CartItemDTO;
import com.anubhav.ecom.dtos.UpdateCartItemRequestDTO;
import com.anubhav.ecom.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController
{
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart()
    {
        CartDTO cartDTO = cartService.createCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDTO> addItemToCart(@PathVariable UUID cartId, @RequestBody AddItemToCartRequestDTO item)
    {
        CartItemDTO cartItem = cartService.addItemToCart(cartId, item);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable UUID cartId)
    {
        CartDTO cartDTO = cartService.getCart(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDTO> updateItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId, @Valid @RequestBody UpdateCartItemRequestDTO request)
    {
        CartItemDTO itemDTO = cartService.updateItem(cartId, productId, request);
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId)
    {
        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId)
    {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

}
