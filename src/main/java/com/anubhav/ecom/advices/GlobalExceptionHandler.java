package com.anubhav.ecom.advices;

import com.anubhav.ecom.exceptions.BadRequestException;
import com.anubhav.ecom.exceptions.ResourceConflictException;
import com.anubhav.ecom.exceptions.ResourceNotFoundException;
import com.anubhav.ecom.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler
{

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
