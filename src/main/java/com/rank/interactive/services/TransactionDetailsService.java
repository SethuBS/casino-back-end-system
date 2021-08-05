package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.TransactionDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransactionDetailsService {

    List<TransactionDetails> getTransactionDetails();

    List<TransactionDetails> getTransactionDetailsByPlayer(Player player);

    void insert(TransactionDetails details);
}
