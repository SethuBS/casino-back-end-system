package com.rank.interactive.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WinRequest(
        @NotBlank String transactionId,
        @NotNull @Positive Long playerId,
        @NotNull @Positive @Digits(integer = 17, fraction = 2) BigDecimal amount)
{
}
