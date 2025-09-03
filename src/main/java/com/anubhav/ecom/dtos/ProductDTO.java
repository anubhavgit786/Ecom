package com.anubhav.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO
{
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Long categoryId;
}
