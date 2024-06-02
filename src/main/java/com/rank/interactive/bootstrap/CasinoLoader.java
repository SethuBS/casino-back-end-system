package com.rank.interactive.bootstrap;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@AllArgsConstructor
@Component
public class CasinoLoader implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void run(String... strings) {
        loadData();
    }

    private void loadData() {
        if (playerRepository.count() == 0) {
            Player player = Player.builder()
                    .id(1L)
                    .username("test_player")
                    .balance(new BigDecimal("500.00"))
                    .freeWagers(5)
                    .build();
            playerRepository.save(player);

            if (transactionRepository.count() == 0) {
                Random random = new Random();
                for (int i = 0; i < 11; i++) {
                    Transaction transaction = Transaction.builder()
                            .id(null)
                            .transactionId("unique-transaction-id-"+i)
                            .playerId(player.getId())
                            .amount(new BigDecimal("50.00"))
                            .type(random.nextBoolean() ? "wager" : "win")
                            .timestamp(LocalDateTime.now())
                            .build();
                    transactionRepository.save(transaction);
                    var newBalance = transaction.getType().equals("wager")
                            ? player.getBalance().subtract(transaction.getAmount())
                            : player.getBalance().add(transaction.getAmount());
                    player.setBalance(newBalance);
                    playerRepository.save(player);
                }
            }
        }
    }
}
