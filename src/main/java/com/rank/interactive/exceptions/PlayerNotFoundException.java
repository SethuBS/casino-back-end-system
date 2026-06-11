package com.rank.interactive.exceptions;

public class PlayerNotFoundException extends RuntimeException
{
    public PlayerNotFoundException(Long playerId)
    {
        super("Player not found: " + playerId);
    }

    public PlayerNotFoundException(String username)
    {
        super("Player not found: " + username);
    }
}
