package com.rank.interactive.Controllers;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.services.MoneyService;
import com.rank.interactive.services.PlayerService;
import com.rank.interactive.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Sethu on 2021/08/04.
 */
@RestController
@RequestMapping("/api/v1/casino")
public class ClientApiController {

    @Autowired
    PlayerService playerService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    MoneyService moneyService;

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
    public ResponseEntity<Player> savePlayer(@PathVariable Player player){
        Player playerToSave = playerService.insert(player);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("player", "/api/v1/casino/player" + playerToSave.getId().toString());
        return new ResponseEntity<>(playerToSave, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping({"/player/{playerId}"})
    public ResponseEntity<Player> updatePlayer(@PathVariable("playerId") Long playerId, @RequestBody Player player){
        playerService.updatePlayer(playerId,player);
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @DeleteMapping({"/{todoId}"})
    public ResponseEntity<Player> deletePlayer(@PathVariable("playerId") Long playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping({"/transaction"})
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transactions = transactionService.getAllTransaction();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/transaction/balance/{playerId}")
    public Double getCurrentBalance(@PathVariable("playerId") Long playerId){
        Player player = playerService.getPlayerById(playerId);
        return moneyService.getBalance(player);
    }

    @PostMapping("/transaction/deposit/{playerId}/{amount}")
    public Double deposit(@PathVariable("playerId") Long playerId, @PathVariable("amount") Double amount){
        Player player = playerService.getPlayerById(playerId);
        return moneyService.deposit(player,amount);
    }

    @PostMapping("/transaction/deduct/{playerId}/{amount}")
    public Double deduct(@PathVariable("playerId") Long playerId, @PathVariable("amount") Double amount){
        Player player = playerService.getPlayerById(playerId);
        return moneyService.deduct(player,amount);
    }
}
