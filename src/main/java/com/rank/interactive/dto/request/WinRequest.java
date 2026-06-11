package com.rank.interactive.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WinRequest(
        @NotBlank String transactionId,
        @NotNull @Positive Long playerId,
        @NotNull @Positive BigDecimal amount)
{
}
