package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import org.springframework.stereotype.Component;


@Component
public interface MoneyService {

    Double getBalance(Player player);
    Double deduct(Transaction transaction, Player player, Double amount);
    Double deposit(Transaction transaction,Player player, Double amount);
    String outOfFunds();
}
