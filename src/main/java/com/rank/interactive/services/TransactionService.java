package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface TransactionService {
    List<Transaction> getAllTransaction();
    List<Transaction> getAllTransactionByPlayer(Player player);
    Transaction TransactionById(Long id);
    Transaction insert(Transaction transaction);
    void updateTransaction(Long id, Transaction transaction);
    void deleteTransaction(Long id);
}
