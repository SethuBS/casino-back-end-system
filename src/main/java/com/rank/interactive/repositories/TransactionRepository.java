package com.rank.interactive.repositories;

import com.rank.interactive.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTop10ByPlayerIdOrderByTimestampDesc(Long playerId);

    Optional<Transaction> findByTransactionId(String transactionId);
}
