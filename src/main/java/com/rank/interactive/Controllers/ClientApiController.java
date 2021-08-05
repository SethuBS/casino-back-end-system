package com.rank.interactive.Controllers;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionDetails;
import com.rank.interactive.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/casino")
public class ClientApiController {

    @Autowired
    PlayerService playerService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    MoneyService moneyService;

    @Autowired
    TransactionDetailsService transactionDetailsService;

    @Autowired
    PassWordValidatorService passWordValidatorService;

    @GetMapping("/transaction/balance/{playerId}")
    public ResponseEntity<Double> getCurrentBalance(@PathVariable("playerId") Long playerId){
        Player player = playerService.getPlayerById(playerId);
        Double amount = moneyService.getBalance(player);
        if(amount<=0){
            return new ResponseEntity<>(amount,HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(moneyService.getBalance(player),HttpStatus.OK);
        }
    }

    @PostMapping("/transaction/deposit/{transactionId}/{playerId}/{amount}")
    public Double deposit(@PathVariable("transactionId")Long transactionId, @PathVariable("playerId") Long playerId, @PathVariable("amount") Double amount){
        Transaction transaction = transactionService.TransactionById(transactionId);
        Player player = playerService.getPlayerById(playerId);
        return moneyService.deposit(transaction,player,amount);
    }

    @PostMapping("/transaction/deduct/{transactionId}/{playerId}/{amount}")
    public Double deduct(@PathVariable("transactionId")Long transactionId, @PathVariable("playerId")  Long playerId, @PathVariable("amount") Double amount){
        Transaction transaction = transactionService.TransactionById(transactionId);
        Player player = playerService.getPlayerById(playerId);
        return moneyService.deduct(transaction,player,amount);
    }

    @PostMapping("/transaction/details/{userName}/{passWord}")
    public ResponseEntity<List<TransactionDetails>> details(@PathVariable("userName") String userName, @PathVariable("passWord") String passWord , @RequestBody Player player){
        player = playerService.findPlayerByUserName(userName);
        String pass = passWordValidatorService.validate(passWord);
        player.setPassWord(pass);
        if(userName.equals(player.getUserName()) && passWord.equals(pass)){
            return new ResponseEntity<>(transactionDetailsService.getTransactionDetailsByPlayer(player),HttpStatus.OK);
        } else {
            return new ResponseEntity<>((List<TransactionDetails>) null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/player"})
    public ResponseEntity<List<Player>> getAllPlayers(){
        List<Player> players = playerService.getPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping({"/player/{playerId}"})
    public ResponseEntity<Player> getPlayer(@PathVariable Long playerId){
        return new ResponseEntity<>(playerService.getPlayerById(playerId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Player> savePlayer(@RequestBody Player player){
        Player playerToSave = playerService.insert(player);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("player", "/api/v1/casino/player/"+playerToSave.getId().toString());
        return new ResponseEntity<>(playerToSave, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping({"/player/{playerId}"})
    public ResponseEntity<Player> updatePlayer(@PathVariable("playerId") Long playerId, @RequestBody Player player){
        playerService.updatePlayer(playerId,player);
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @DeleteMapping({"/{playerId}"})
    public ResponseEntity<Player> deletePlayer(@PathVariable("playerId") Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping({"/transaction"})
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transactions = transactionService.getAllTransaction();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping({"/transaction"})
    public Transaction saveTransaction(@RequestBody Transaction transaction){
       return transactionService.insert(transaction);
    }

    @PutMapping({"/transaction/{transactionId}"})
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("transactionId") Long transactionId, @RequestBody Transaction transaction){
        transactionService.updateTransaction(transactionId,transaction);
        return new ResponseEntity<>(transactionService.TransactionById(transactionId), HttpStatus.OK);
    }

    @DeleteMapping({"/{transactionId}"})
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable("transactionId") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
