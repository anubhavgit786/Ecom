package com.anubhav.ecom.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException
{
    private HttpStatus status;
    public ResourceNotFoundException(String message)
    {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
