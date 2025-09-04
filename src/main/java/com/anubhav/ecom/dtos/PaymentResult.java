package com.anubhav.ecom.dtos;

import com.anubhav.ecom.models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResult
{
    private Long orderId;
    private PaymentStatus status;
}
