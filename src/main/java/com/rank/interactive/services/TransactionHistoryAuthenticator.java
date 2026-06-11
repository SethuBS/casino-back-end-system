package com.rank.interactive.services;

import com.rank.interactive.config.CasinoProperties;
import com.rank.interactive.exceptions.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RequiredArgsConstructor
@Service
public class TransactionHistoryAuthenticator
{
    private final CasinoProperties casinoProperties;

    public void authenticate(String password)
    {
        if (!matches(password, casinoProperties.getAuth().getTransactionHistoryPassword()))
        {
            throw new InvalidPasswordException();
        }
    }

    private boolean matches(String providedPassword, String configuredPassword)
    {
        if (!StringUtils.hasText(providedPassword) || !StringUtils.hasText(configuredPassword))
        {
            return false;
        }

        return MessageDigest.isEqual(
                providedPassword.getBytes(StandardCharsets.UTF_8),
                configuredPassword.getBytes(StandardCharsets.UTF_8));
    }
}
