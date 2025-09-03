package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.AddItemToCartRequestDTO;
import com.anubhav.ecom.dtos.CartDTO;
import com.anubhav.ecom.dtos.CartItemDTO;
import com.anubhav.ecom.dtos.UpdateCartItemRequestDTO;
import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.exceptions.ResourceNotFoundException;
import com.anubhav.ecom.models.Cart;
import com.anubhav.ecom.repositories.CartRepository;
import com.anubhav.ecom.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService
{
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO createCart()
    {
        var cart = new Cart();
        var savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart, CartDTO.class);
    }

    @Override
    public CartItemDTO addItemToCart(UUID cartId, AddItemToCartRequestDTO item)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
        var product = productRepository.findById(item.getProductId()).orElseThrow(() -> new BadRequestException("Invalid product id  : " + item.getProductId()));
        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        var cartItemDto =  modelMapper.map(cartItem, CartItemDTO.class);
        cartItemDto.setTotalPrice(cartItemDto.getTotalPrice());
        return cartItemDto;
    }

    @Override
    public CartDTO getCart(UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
        var cartDTO =  modelMapper.map(cart, CartDTO.class);
        cartDTO.setTotalPrice(cart.getTotalPrice());
        return cartDTO;
    }

    @Override
    public CartItemDTO updateItem(UUID cartId, Long productId, UpdateCartItemRequestDTO request)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
        var cartItem = cart.getItem(productId);

        if(cartItem == null)
        {
            throw new ResourceNotFoundException("Product not found in the cart");
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        var cartItemDto =  modelMapper.map(cartItem, CartItemDTO.class);
        cartItemDto.setTotalPrice(cartItemDto.getTotalPrice());
        return cartItemDto;
    }

    @Override
    public void removeItem(UUID cartId, Long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
        cart.removeItem(productId);
        cartRepository.save(cart);

    }

    @Override
    public void clearCart(UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id : " + cartId));
        cart.clear();
        cartRepository.save(cart);
    }
}
