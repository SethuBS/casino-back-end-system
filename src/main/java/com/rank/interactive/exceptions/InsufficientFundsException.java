package com.rank.interactive.exceptions;

public class InsufficientFundsException extends RuntimeException
{
    public InsufficientFundsException(Long playerId)
    {
        super("Insufficient funds for player: " + playerId);
    }
}
