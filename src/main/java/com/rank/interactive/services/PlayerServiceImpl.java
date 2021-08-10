package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository repository;

    @Override
    public List<Player> getPlayers() {

        List<Player> players = new ArrayList<>();
        repository.findAll().forEach(players::add);
        return players;
    }

    @Override
    public Player getPlayerById(Long id) {
        Player player = null;

        List<Player> players = getPlayers();

        for(Player player1: players){
            if(player1.getId().equals(id)){
                player = player1;
            }
        }
        return player;
    }

    @Override
    public Player findPlayerByUserName(String userName) {
        Player player = null;

        List<Player> players = getPlayers();


        for (Player player1: players){
            if(player1.getUserName().equals(userName)){
                player = player1;
            }
        }
        return player;
    }

    @Override
    public Player insert(Player player) {
        return repository.save(player);
    }

    @Override
    public void updatePlayer(Long id, Player player) {

        Player playerToUpdate = repository.findById(id).get();
        System.out.println(playerToUpdate.toString());
        playerToUpdate.setUserName(player.getUserName());
        playerToUpdate.setPassWord(player.getPassWord());
        playerToUpdate.setEmailAddress(player.getEmailAddress());
        repository.save(playerToUpdate);

    }

    @Override
    public void deletePlayer(Long playerId) {
        repository.deleteById(playerId);
    }
}
