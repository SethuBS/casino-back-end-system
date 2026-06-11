package com.rank.interactive.services;

import com.rank.interactive.config.CasinoProperties;
import com.rank.interactive.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionHistoryAuthenticatorTests
{
    @Test
    void authenticateRejectsBlankConfiguredPassword()
    {
        TransactionHistoryAuthenticator authenticator = authenticatorWithConfiguredPassword("");

        assertThatThrownBy(() -> authenticator.authenticate(""))
                .isInstanceOf(InvalidPasswordException.class);
        assertThatThrownBy(() -> authenticator.authenticate("anything"))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void authenticateAcceptsMatchingConfiguredPassword()
    {
        TransactionHistoryAuthenticator authenticator = authenticatorWithConfiguredPassword("history-password");

        assertThatNoException().isThrownBy(() -> authenticator.authenticate("history-password"));
    }

    private TransactionHistoryAuthenticator authenticatorWithConfiguredPassword(String password)
    {
        CasinoProperties casinoProperties = new CasinoProperties();
        casinoProperties.getAuth().setTransactionHistoryPassword(password);
        return new TransactionHistoryAuthenticator(casinoProperties);
    }
}
