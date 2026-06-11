package com.rank.interactive.model;

public enum PromotionCode
{
    PAPER;

    public boolean matches(String promotionCode, String configuredCode)
    {
        return configuredCode.equalsIgnoreCase(promotionCode);
    }
}
