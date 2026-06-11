package com.rank.interactive.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasinoApiProperties
{
    @NotBlank
    private String basePath;

    @NotBlank
    private String balancePath;

    @NotBlank
    private String wagerPath;

    @NotBlank
    private String winPath;

    @NotBlank
    private String transactionsPath;
}
