package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface PlayerService {

    List<Player> getPlayers();

    Player getPlayerById(Long id);

    Player findPlayerByUserName(String userName);

    Player insert(Player player);

    void updatePlayer(Long id, Player player);

    void deletePlayer(Long playerId);
}
