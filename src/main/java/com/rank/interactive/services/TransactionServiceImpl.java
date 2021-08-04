package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sethu on 2021/08/04.
 */
@Service
public class TransactionServiceImpl implements TransactionService,MoneyService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransaction() {

        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactionByPlayer(Player player) {

        List<Transaction> transactions = new ArrayList<>();
        for(Transaction transaction: transactionRepository.findAll()){
            if(transaction.getPlayer().equals(player)){
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public Transaction TransactionById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    public Transaction insert(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void updateTransaction(Long id, Transaction transaction) {
        Transaction transactionToUpdate = transactionRepository.findById(id).get();
        System.out.println(transactionToUpdate.toString());
        transactionToUpdate.setPlayer(transaction.getPlayer());
        transactionToUpdate.setBalance(transaction.getBalance());
        transactionRepository.save(transactionToUpdate);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Double getBalance(Player player) {
        Double currentBalance = 0d;
        for(Transaction transaction: getAllTransactionByPlayer(player)){
            if(transaction.getBalance()!=0){
                currentBalance += transaction.getBalance();
            }
        }
        return currentBalance;
    }

    @Override
    public Double deduct(Player player, Double amount) {
        if(amount > getBalance(player)){
            return 0d;
        } else {
            return getBalance(player) - amount;
        }
    }

    @Override
    public Double deposit(Player player, Double amount) {

        Transaction transaction = new Transaction();
        transaction.setPlayer(player);
        transaction.setBalance(amount);
        transactionRepository.save(transaction);

        return  getBalance(player);
    }
}
