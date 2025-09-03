package com.anubhav.ecom.dtos;


import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartDTO
{
    private UUID id;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
