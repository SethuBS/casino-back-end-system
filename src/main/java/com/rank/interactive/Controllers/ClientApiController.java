package com.rank.interactive.Controllers;

import com.rank.interactive.model.Player;
import com.rank.interactive.services.PlayerService;
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
}
