package com.rank.interactive.services;

import com.rank.interactive.exceptions.InsufficientFundsException;
import com.rank.interactive.exceptions.PlayerNotFoundException;
import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionType;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties =
{
        "casino.auth.transaction-history-password=history-password",
        "casino.promotion.free-wager-code=free-spin",
        "casino.promotion.free-wagers-awarded=5",
        "casino.transaction-history.limit=10"
})
class CasinoServiceIntegrationTests
{
    @Autowired
    private CasinoService casinoService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp()
    {
        transactionRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void processWagerDeductsBalanceAndStoresWagerTransaction()
    {
        Player player = savePlayer("wager_player", "100.00", 0);

        casinoService.processWager("wager-transaction", player.getId(), amount("25.50"), null);

        Player updatedPlayer = findPlayer(player.getId());
        Transaction transaction = findTransaction("wager-transaction");

        assertThat(updatedPlayer.getBalance()).isEqualByComparingTo("74.50");
        assertThat(transaction.getPlayerId()).isEqualTo(player.getId());
        assertThat(transaction.getAmount()).isEqualByComparingTo("-25.50");
        assertThat(transaction.getType()).isEqualTo(TransactionType.WAGER);
    }

    @Test
    void processWinAddsBalanceAndStoresWinTransaction()
    {
        Player player = savePlayer("win_player", "100.00", 0);

        casinoService.processWin("win-transaction", player.getId(), amount("42.25"));

        Player updatedPlayer = findPlayer(player.getId());
        Transaction transaction = findTransaction("win-transaction");

        assertThat(updatedPlayer.getBalance()).isEqualByComparingTo("142.25");
        assertThat(transaction.getPlayerId()).isEqualTo(player.getId());
        assertThat(transaction.getAmount()).isEqualByComparingTo("42.25");
        assertThat(transaction.getType()).isEqualTo(TransactionType.WIN);
    }

    @Test
    void processWagerIgnoresDuplicateTransactionId()
    {
        Player player = savePlayer("duplicate_player", "100.00", 0);

        casinoService.processWager("duplicate-transaction", player.getId(), amount("30.00"), null);
        casinoService.processWager("duplicate-transaction", player.getId(), amount("30.00"), null);

        Player updatedPlayer = findPlayer(player.getId());

        assertThat(updatedPlayer.getBalance()).isEqualByComparingTo("70.00");
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(findTransaction("duplicate-transaction").getAmount()).isEqualByComparingTo("-30.00");
    }

    @Test
    void processWagerRejectsInsufficientFundsWithoutChangingBalance()
    {
        Player player = savePlayer("insufficient_player", "20.00", 0);

        assertThatThrownBy(() -> casinoService.processWager(
                "insufficient-transaction",
                player.getId(),
                amount("25.00"),
                null))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds for player: " + player.getId());

        assertThat(findPlayer(player.getId()).getBalance()).isEqualByComparingTo("20.00");
        assertThat(transactionRepository.findByTransactionId("insufficient-transaction")).isEmpty();
    }

    @Test
    void processWinRejectsInvalidPlayer()
    {
        long missingPlayerId = 999999L;

        assertThatThrownBy(() -> casinoService.processWin("missing-player-transaction", missingPlayerId, amount("10.00")))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage("Player not found: " + missingPlayerId);

        assertThat(transactionRepository.count()).isZero();
    }

    @Test
    void getRecentTransactionsReturnsLastTenOrderedByNewestFirst()
    {
        Player player = savePlayer("history_player", "100.00", 0);
        LocalDateTime baseTimestamp = LocalDateTime.parse("2026-06-11T10:00:00");

        for (int index = 1; index <= 12; index++)
        {
            transactionRepository.save(Transaction.builder()
                    .transactionId("history-" + String.format("%02d", index))
                    .playerId(player.getId())
                    .amount(amount(String.valueOf(index)))
                    .type(index % 2 == 0 ? TransactionType.WIN : TransactionType.WAGER)
                    .timestamp(baseTimestamp.plusMinutes(index))
                    .build());
        }

        List<Transaction> transactions = casinoService.getRecentTransactions("history_player", "history-password");

        assertThat(transactions).hasSize(10);
        assertThat(transactions)
                .extracting(Transaction::getTransactionId)
                .containsExactly(
                        "history-12",
                        "history-11",
                        "history-10",
                        "history-09",
                        "history-08",
                        "history-07",
                        "history-06",
                        "history-05",
                        "history-04",
                        "history-03");
    }

    @Test
    void processWagerConsumesStoredFreeWagerWithoutDeductingBalance()
    {
        Player player = savePlayer("promotion_player", "100.00", 1);

        casinoService.processWager("promotion-transaction", player.getId(), amount("40.00"), null);

        Player updatedPlayer = findPlayer(player.getId());
        Transaction transaction = findTransaction("promotion-transaction");

        assertThat(updatedPlayer.getBalance()).isEqualByComparingTo("100.00");
        assertThat(updatedPlayer.getFreeWagers()).isZero();
        assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(transaction.getType()).isEqualTo(TransactionType.WAGER);
    }

    @Test
    void processWagerWithConfiguredPromotionGrantsFreeWagersAndUsesOneImmediately()
    {
        Player player = savePlayer("promotion_redeem_player", "100.00", 0);

        casinoService.processWager("promotion-redeem-transaction", player.getId(), amount("40.00"), "free-spin");

        Player updatedPlayer = findPlayer(player.getId());
        Transaction transaction = findTransaction("promotion-redeem-transaction");

        assertThat(updatedPlayer.getBalance()).isEqualByComparingTo("100.00");
        assertThat(updatedPlayer.getFreeWagers()).isEqualTo(4);
        assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(transaction.getType()).isEqualTo(TransactionType.WAGER);
    }

    @Test
    void stalePlayerBalanceUpdateFailsOptimisticLocking()
    {
        Player player = savePlayer("locked_player", "100.00", 0);
        Player firstCopy = findPlayer(player.getId());
        Player staleCopy = findPlayer(player.getId());

        firstCopy.setBalance(amount("90.00"));
        playerRepository.save(firstCopy);

        staleCopy.setBalance(amount("80.00"));

        assertThatThrownBy(() -> playerRepository.save(staleCopy))
                .isInstanceOf(OptimisticLockingFailureException.class);

        assertThat(findPlayer(player.getId()).getBalance()).isEqualByComparingTo("90.00");
    }

    private Player savePlayer(String username, String balance, int freeWagers)
    {
        return playerRepository.save(Player.builder()
                .username(username)
                .balance(amount(balance))
                .freeWagers(freeWagers)
                .build());
    }

    private Player findPlayer(Long playerId)
    {
        return playerRepository.findById(playerId).orElseThrow();
    }

    private Transaction findTransaction(String transactionId)
    {
        return transactionRepository.findByTransactionId(transactionId).orElseThrow();
    }

    private BigDecimal amount(String value)
    {
        return new BigDecimal(value);
    }
}
