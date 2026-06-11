package com.rank.interactive.dto.response;

import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        Long playerId,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime timestamp)
{
    public static TransactionResponse from(Transaction transaction)
    {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getPlayerId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTimestamp());
    }
}
