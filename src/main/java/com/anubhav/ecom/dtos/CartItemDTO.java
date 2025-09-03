package com.anubhav.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO
{
    private CartProductDTO product;
    private int quantity;
    private BigDecimal totalPrice;
}
