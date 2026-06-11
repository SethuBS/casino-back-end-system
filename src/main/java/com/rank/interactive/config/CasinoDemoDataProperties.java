package com.rank.interactive.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CasinoDemoDataProperties
{
    private boolean enabled;

    @NotNull
    @Positive
    private Long playerId;

    @NotBlank
    private String username;

    @NotNull
    @Positive
    private BigDecimal startingBalance;

    @PositiveOrZero
    private int initialFreeWagers;

    @Positive
    private int transactionCount;

    @NotNull
    @Positive
    private BigDecimal transactionAmount;

    @NotBlank
    private String transactionIdPrefix;
}
