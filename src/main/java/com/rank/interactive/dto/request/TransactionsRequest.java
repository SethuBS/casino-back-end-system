package com.rank.interactive.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TransactionsRequest(
        @NotBlank String username,
        @NotBlank String password)
{
}
