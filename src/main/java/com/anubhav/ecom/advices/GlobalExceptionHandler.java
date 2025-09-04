package com.anubhav.ecom.advices;

import com.anubhav.ecom.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.stripe.exception.StripeException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    // Handle StripeException
    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<Map<String, Object>> handleStripeException(PaymentGatewayException ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Payment Gateway Error");
        body.put("message", ex.getMessage());
        body.put("provider", ex.getProvider());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<?>> handleForbiddenException(ForbiddenException ex)
    {
        ApiResponse<?> response = new ApiResponse<>(ex.getMessage(), ex.getStatus(), null);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException()
    {
        ApiResponse<?> response = new ApiResponse<>("Invalid Credentials", HttpStatus.UNAUTHORIZED, null);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex)
    {
        ApiResponse<?> response = new ApiResponse<>(ex.getMessage(), ex.getStatus(), null);
        return new ResponseEntity<>(response, response.getStatus());
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex)
    {
        ApiResponse<?> response = new ApiResponse<>(ex.getMessage(), ex.getStatus(), null);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceConflictException(ResourceConflictException ex)
    {
        ApiResponse<?> response = new ApiResponse<>(ex.getMessage(), ex.getStatus(), null);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        ApiResponse<?> response = new ApiResponse<>(ex.getMessage(), ex.getStatus(), null);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationErrors(MethodArgumentNotValidException exception)
    {
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<?> response = new ApiResponse<>("Input validation failed", HttpStatus.BAD_REQUEST, errors);

        return new ResponseEntity<>(response, response.getStatus());
    }

}
