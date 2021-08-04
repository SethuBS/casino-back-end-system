package com.rank.interactive.bootstrap;

import com.rank.interactive.model.Player;
import com.rank.interactive.repositories.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Sethu on 2021/08/04.
 */
@Component
public class PlayerLoader implements CommandLineRunner {

    private  final PlayerRepository playerRepository;

    public PlayerLoader(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        loadPlayer();
    }

    private void loadPlayer() {
        if(playerRepository.count() == 0){
            Player player1 = new Player();
            player1.setUserName("Sethu");
            player1.setPassWord("Sethu123");
            player1.setEmailAddress("sethu@gmail.com");
            playerRepository.save(player1);

            Player player2 = new Player();
            player2.setUserName("Serge");
            player2.setPassWord("Serge123");
            player2.setEmailAddress("Serge@gmail.com");
            playerRepository.save(player2);
        }
    }
}
