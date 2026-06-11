package com.rank.interactive.services;

import com.rank.interactive.config.CasinoProperties;
import com.rank.interactive.exceptions.InsufficientFundsException;
import com.rank.interactive.exceptions.InvalidPasswordException;
import com.rank.interactive.exceptions.PlayerNotFoundException;
import com.rank.interactive.model.Player;
import com.rank.interactive.model.PromotionCode;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionType;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CasinoService
{
    private final PlayerRepository playerRepository;

    private final TransactionRepository transactionRepository;

    private final CasinoProperties casinoProperties;

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long playerId)
    {
        return findPlayer(playerId).getBalance();
    }

    @Transactional
    public void processWager(String transactionId, Long playerId, BigDecimal amount, String promotionCode)
    {
        if (transactionRepository.findByTransactionId(transactionId).isPresent())
        {
            return; // Idempotent
        }

        Player player = findPlayer(playerId);

        if (PromotionCode.FREE_WAGER.matches(
                promotionCode,
                casinoProperties.getPromotion().getFreeWagerCode()) && player.getFreeWagers() > 0)
        {
            player.setFreeWagers(player.getFreeWagers() - 1);
            amount = BigDecimal.ZERO; // Make wager free
        }
        else if (player.getBalance().compareTo(amount) < 0)
        {
            throw new InsufficientFundsException(playerId);
        }
        else
        {
            player.setBalance(player.getBalance().subtract(amount));
        }

        playerRepository.save(player);
        transactionRepository.save(createTransaction(transactionId, playerId, amount.negate(), TransactionType.WAGER));
    }

    @Transactional
    public void processWin(String transactionId, Long playerId, BigDecimal amount)
    {
        if (transactionRepository.findByTransactionId(transactionId).isPresent())
        {
            return; // Idempotent
        }

        Player player = findPlayer(playerId);

        player.setBalance(player.getBalance().add(amount));
        playerRepository.save(player);
        transactionRepository.save(createTransaction(transactionId, playerId, amount, TransactionType.WIN));
    }

    @Transactional(readOnly = true)
    public List<Transaction> getRecentTransactions(String username, String password)
    {
        if (!casinoProperties.getAuth().getTransactionHistoryPassword().equals(password))
        {
            throw new InvalidPasswordException();
        }

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException(username));

        return transactionRepository.findByPlayerIdOrderByTimestampDesc(
                player.getId(),
                PageRequest.ofSize(casinoProperties.getTransactionHistory().getLimit()));
    }

    private Player findPlayer(Long playerId)
    {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    private Transaction createTransaction(
            String transactionId,
            Long playerId,
            BigDecimal amount,
            TransactionType transactionType)
    {
        return Transaction.builder()
                .transactionId(transactionId)
                .playerId(playerId)
                .amount(amount)
                .type(transactionType)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
