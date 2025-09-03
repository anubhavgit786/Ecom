package com.anubhav.ecom.controllers;

import com.anubhav.ecom.dtos.CheckoutRequestDTO;
import com.anubhav.ecom.dtos.CheckoutResponseDTO;
import com.anubhav.ecom.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController
{
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> checkout(@Valid @RequestBody CheckoutRequestDTO checkoutRequestDTO)
    {
        CheckoutResponseDTO response = checkoutService.checkout(checkoutRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
