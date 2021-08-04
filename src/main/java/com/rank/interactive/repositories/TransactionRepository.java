package com.rank.interactive.repositories;

import com.rank.interactive.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sethu on 2021/08/04.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
