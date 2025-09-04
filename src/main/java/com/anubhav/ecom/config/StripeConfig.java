package com.anubhav.ecom.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class StripeConfig
{
    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init()
    {
        Stripe.apiKey = secretKey;
    }
}
