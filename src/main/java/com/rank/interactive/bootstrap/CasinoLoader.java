package com.rank.interactive.bootstrap;

import com.rank.interactive.config.CasinoDemoDataProperties;
import com.rank.interactive.config.CasinoProperties;
import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionType;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class CasinoLoader implements CommandLineRunner
{
    private final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;
    private final CasinoProperties casinoProperties;

    @Override
    public void run(String... strings)
    {
        loadData();
    }

    private void loadData()
    {
        CasinoDemoDataProperties demoData = casinoProperties.getDemoData();

        if (!demoData.isEnabled())
        {
            return;
        }

        if (playerRepository.count() == 0)
        {
            Player player = Player.builder()
                    .id(demoData.getPlayerId())
                    .username(demoData.getUsername())
                    .balance(demoData.getStartingBalance())
                    .freeWagers(demoData.getInitialFreeWagers())
                    .build();
            playerRepository.save(player);

            if (transactionRepository.count() == 0)
            {
                Random random = new Random();
                for (int i = 0; i < demoData.getTransactionCount(); i++)
                {
                    Transaction transaction = Transaction.builder()
                            .id(null)
                            .transactionId(demoData.getTransactionIdPrefix() + i)
                            .playerId(player.getId())
                            .amount(demoData.getTransactionAmount())
                            .type(random.nextBoolean() ? TransactionType.WAGER : TransactionType.WIN)
                            .timestamp(LocalDateTime.now())
                            .build();
                    transactionRepository.save(transaction);
                    var newBalance = transaction.getType() == TransactionType.WAGER
                            ? player.getBalance().subtract(transaction.getAmount())
                            : player.getBalance().add(transaction.getAmount());
                    player.setBalance(newBalance);
                    playerRepository.save(player);
                }
            }
        }
    }
}
