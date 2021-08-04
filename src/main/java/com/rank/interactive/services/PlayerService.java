package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Sethu on 2021/08/04.
 */
@Component
public interface PlayerService {

    List<Player> getPlayers();

    Player getPlayerById(Long id);

    Player insert(Player player);

    void updatePlayer(Long id, Player player);

    void deletePlayer(Long playerId);
}
