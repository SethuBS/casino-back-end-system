package com.rank.interactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties =
{
        "casino.auth.transaction-history-password=test-password",
        "casino.promotion.free-wager-code=test-promotion"
})
class CasinoBackEndSystemContextTests
{
    @Test
    void contextLoads()
    {
    }
}
