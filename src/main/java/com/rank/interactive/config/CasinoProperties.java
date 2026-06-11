package com.rank.interactive.config;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "casino")
public class CasinoProperties
{
    @Valid
    private CasinoApiProperties api = new CasinoApiProperties();

    @Valid
    private CasinoAuthProperties auth = new CasinoAuthProperties();

    @Valid
    private CasinoPromotionProperties promotion = new CasinoPromotionProperties();

    @Valid
    private CasinoTransactionHistoryProperties transactionHistory = new CasinoTransactionHistoryProperties();

    @Valid
    private CasinoDemoDataProperties demoData = new CasinoDemoDataProperties();
}
