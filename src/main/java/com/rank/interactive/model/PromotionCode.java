package com.rank.interactive.model;

public enum PromotionCode
{
    FREE_WAGER;

    public boolean matches(String promotionCode, String configuredCode)
    {
        return configuredCode.equalsIgnoreCase(promotionCode);
    }
}
