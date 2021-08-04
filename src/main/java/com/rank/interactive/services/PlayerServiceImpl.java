package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import com.rank.interactive.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sethu on 2021/08/04.
 */
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
        return repository.findById(id).get();
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
