package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Service
public class CasinoService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal getBalance(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found"))
                .getBalance();
    }

    public void processWager(String transactionId, Long playerId, BigDecimal amount, String promotionCode) {
        if (transactionRepository.findByTransactionId(transactionId).isPresent()) {
            return; // Idempotent
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found"));

        if ("paper".equals(promotionCode) && player.getFreeWagers() > 0) {
            player.setFreeWagers(player.getFreeWagers() - 1);
            amount = BigDecimal.ZERO; // Make wager free
        } else if (player.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Insufficient funds");
        } else {
            player.setBalance(player.getBalance().subtract(amount));
        }

        playerRepository.save(player);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setPlayerId(playerId);
        transaction.setAmount(amount.negate());
        transaction.setType("wager");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void processWin(String transactionId, Long playerId, BigDecimal amount) {
        if (transactionRepository.findByTransactionId(transactionId).isPresent()) {
            return; // Idempotent
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found"));

        player.setBalance(player.getBalance().add(amount));
        playerRepository.save(player);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setPlayerId(playerId);
        transaction.setAmount(amount);
        transaction.setType("win");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public List<Transaction> getLastTenTransactions(String username, String password) {
        if (!"swordfish".equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found"));

        return transactionRepository.findTop10ByPlayerIdOrderByTimestampDesc(player.getId());
    }
}
