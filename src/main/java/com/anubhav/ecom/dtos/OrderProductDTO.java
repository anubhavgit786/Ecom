package com.anubhav.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDTO
{
    private Long id;
    private String name;
    private BigDecimal price;
}
