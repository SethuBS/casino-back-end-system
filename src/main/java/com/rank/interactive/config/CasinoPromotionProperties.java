package com.rank.interactive.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasinoPromotionProperties
{
    @NotBlank
    private String freeWagerCode;

    @Positive
    private int freeWagersAwarded;
}
