package com.rank.interactive.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp)
{
    public static ApiErrorResponse of(HttpStatus status, String message)
    {
        return new ApiErrorResponse(status.value(), status.getReasonPhrase(), message, LocalDateTime.now());
    }
}
