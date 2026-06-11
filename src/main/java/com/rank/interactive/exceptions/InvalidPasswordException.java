package com.rank.interactive.exceptions;

public class InvalidPasswordException extends RuntimeException
{
    public InvalidPasswordException()
    {
        super("Invalid password");
    }
}
