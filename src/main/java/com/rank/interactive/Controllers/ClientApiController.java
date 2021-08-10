package com.rank.interactive.Controllers;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.services.*;
import org.json.JSONObject;
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
    public ResponseEntity getCurrentBalance(@PathVariable("playerId") Long playerId){
        Player player = playerService.getPlayerById(playerId);
        Double amount = moneyService.getBalance(player);
        if(player == null){
            return  new ResponseEntity<>(player,HttpStatus.BAD_REQUEST);
        }
        if(amount<=0){
            return new ResponseEntity<>(moneyService.outOfFunds(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(moneyService.getBalance(player),HttpStatus.OK);
        }
    }

    @PostMapping("/transaction/deposit/{transactionId}/{playerId}/{amount}")
    public ResponseEntity deposit(@PathVariable("transactionId")Long transactionId, @PathVariable("playerId") Long playerId, @PathVariable("amount") Double amount){
        Transaction transaction = transactionService.TransactionById(transactionId);
        Player player = playerService.getPlayerById(playerId);
        if(player == null){
            return  new ResponseEntity<>(player,HttpStatus.BAD_REQUEST);
        } else if(transaction == null){
            return  new ResponseEntity<>(transaction,HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(moneyService.deposit(transaction,player,amount), HttpStatus.OK);
        }

    }

    @PostMapping("/transaction/deduct/{transactionId}/{playerId}/{amount}")
    public ResponseEntity deduct(@PathVariable("transactionId")Long transactionId, @PathVariable("playerId")  Long playerId, @PathVariable("amount") Double amount){
        Transaction transaction = transactionService.TransactionById(transactionId);
        Player player = playerService.getPlayerById(playerId);

        if(player == null){
            return  new ResponseEntity<>(player,HttpStatus.BAD_REQUEST);
        } else if(transaction == null){
            return  new ResponseEntity<>(transaction,HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(moneyService.deduct(transaction,player,amount), HttpStatus.OK);
        }
    }

    @PostMapping("/transaction/details")
    public ResponseEntity details(@RequestBody String details){

        JSONObject jsonObject = new JSONObject(details);

        String validPassword = passWordValidatorService.getPassword();

        Player player = playerService.findPlayerByUserName(jsonObject.getString("userName"));

        if(player == null){
            return new ResponseEntity<>(details,HttpStatus.BAD_REQUEST);
        }

        if(!(jsonObject.getString("passWord").equals(validPassword)) || !jsonObject.getString("userName").equals(player.getUserName())){
            return new ResponseEntity<>(details,HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(transactionDetailsService.getTransactionDetailsByPlayer(player),HttpStatus.OK);
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
