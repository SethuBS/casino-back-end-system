package com.rank.interactive.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum TransactionType
{
    WAGER,
    WIN;

    @JsonValue
    public String value()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
