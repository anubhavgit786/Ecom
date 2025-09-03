package com.anubhav.ecom.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDTO
{
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
}
