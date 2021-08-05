package com.rank.interactive.repositories;


import com.rank.interactive.model.TransactionDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository  extends CrudRepository<TransactionDetails, Long> {

}
