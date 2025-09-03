package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.CheckoutRequestDTO;
import com.anubhav.ecom.dtos.CheckoutResponseDTO;

public interface CheckoutService
{
    CheckoutResponseDTO checkout(CheckoutRequestDTO checkoutRequestDTO);
}
