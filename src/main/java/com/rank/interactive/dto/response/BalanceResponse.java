package com.rank.interactive.dto.response;

import java.math.BigDecimal;

public record BalanceResponse(Long playerId, BigDecimal balance)
{
}
