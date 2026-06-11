package com.rank.interactive.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasinoAuthProperties
{
    @NotBlank
    private String transactionHistoryPassword;
}
