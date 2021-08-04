package com.rank.interactive.bootstrap;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Sethu on 2021/08/04.
 */
@Component
public class CasinoLoader implements CommandLineRunner {

    private  final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;

    public CasinoLoader(PlayerRepository playerRepository, TransactionRepository transactionRepository){
        this.playerRepository = playerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        loadData();
    }

    private void loadData() {
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

            if(transactionRepository.count() ==0){

                Transaction transaction1 = new Transaction();
                transaction1.setPlayer(player1);
                transaction1.setBalance(2000d);
                transactionRepository.save(transaction1);

                Transaction transaction2 = new Transaction();
                transaction2.setPlayer(player1);
                transaction2.setBalance(2100d);
                transactionRepository.save(transaction2);

                Transaction transaction3 = new Transaction();
                transaction3.setPlayer(player1);
                transaction3.setBalance(2200d);
                transactionRepository.save(transaction3);

                Transaction transaction4 = new Transaction();
                transaction4.setPlayer(player1);
                transaction4.setBalance(2300d);
                transactionRepository.save(transaction4);

                Transaction transaction5 = new Transaction();
                transaction5.setPlayer(player1);
                transaction5.setBalance(2400d);
                transactionRepository.save(transaction5);


            }
        }
    }
}
