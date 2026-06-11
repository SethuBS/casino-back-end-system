package com.rank.interactive.controller;

import com.rank.interactive.dto.response.ApiErrorResponse;
import com.rank.interactive.exceptions.InsufficientFundsException;
import com.rank.interactive.exceptions.InvalidPasswordException;
import com.rank.interactive.exceptions.PlayerNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler
{
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePlayerNotFound(PlayerNotFoundException exception)
    {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPassword(InvalidPasswordException exception)
    {
        return error(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientFunds(InsufficientFundsException exception)
    {
        return error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleOptimisticLocking(ObjectOptimisticLockingFailureException exception)
    {
        return error(HttpStatus.CONFLICT, "Balance was updated by another request. Please retry.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException exception)
    {
        return error(HttpStatus.CONFLICT, "Request conflicts with existing data");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception)
    {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Request validation failed");

        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception)
    {
        String message = exception.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .orElse("Request validation failed");

        return error(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String message)
    {
        return ResponseEntity.status(status).body(ApiErrorResponse.of(status, message));
    }
}
