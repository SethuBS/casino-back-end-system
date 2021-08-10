package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionDetails;
import com.rank.interactive.model.TransactionDetailsStatus;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionDetailsRepository;
import com.rank.interactive.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TransactionServiceImpl implements TransactionService,MoneyService,TransactionDetailsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    PlayerRepository playerRepository;

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
    public Double deduct(Transaction transaction,Player player, Double amount) {

        Transaction transaction1 = transactionRepository.findById(transaction.getId()).get();
        Player player1 = playerRepository.findById(player.getId()).get();
        if(!Objects.isNull(transaction1) && !Objects.isNull(player1)){
            if(amount >= getBalance(player)){
                transaction1.setBalance(0d);
            } else {
                Double newDeposit = transaction1.getBalance() - amount;
                transaction1.setBalance(newDeposit);
            }
            transactionRepository.save(transaction1);
            TransactionDetails details = new TransactionDetails();
            details.setTransaction(transaction1.getId());
            details.setPlayer(player1.getId());
            details.setTransactionDetailsStatus(TransactionDetailsStatus.DEDUCTED);
            details.setAmount(amount);
            insert(details);
        }

        return  getBalance(player);
    }

    @Override
    public Double deposit(Transaction transaction, Player player, Double amount) {

        Transaction transaction1 = transactionRepository.findById(transaction.getId()).get();
        Player player1 = playerRepository.findById(player.getId()).get();
        if(!Objects.isNull(transaction1) && !Objects.isNull(player1)){
            Double newDeposit = transaction1.getBalance() + amount;
            transaction1.setBalance(newDeposit);
            transactionRepository.save(transaction1);
            TransactionDetails details = new TransactionDetails();
            details.setTransaction(transaction1.getId());
            details.setPlayer(player1.getId());
            details.setTransactionDetailsStatus(TransactionDetailsStatus.DEPOSITED);
            details.setAmount(amount);
            insert(details);
        }

        return  getBalance(player);
    }

    @Override
    public String outOfFunds() {
        return "It is a teapot (418)";
    }

    @Override
    public List<TransactionDetails> getTransactionDetails() {

        List<TransactionDetails> transactionDetails = new ArrayList<>();
        transactionDetailsRepository.findAll().forEach(transactionDetails::add);
        return transactionDetails;
    }

    @Override
    public List<TransactionDetails> getTransactionDetailsByPlayer(Player player) {


        List<TransactionDetails> transactionDetails = new ArrayList<>();
        for(TransactionDetails transactionDetail: getTransactionDetails()){
            if(transactionDetail.getPlayer().equals(player.getId())){
                transactionDetails.add(transactionDetail);
            }
        }

        Collections.reverse(transactionDetails);
        if(transactionDetails.size() >= 10){
            transactionDetails =  transactionDetails.subList(0,10);
        }

        return transactionDetails;
    }

    @Override
    public void insert(TransactionDetails details) {
        transactionDetailsRepository.save(details);
    }


}
