package com.rank.interactive.config;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasinoTransactionHistoryProperties
{
    @Positive
    private int limit;
}
