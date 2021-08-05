package com.rank.interactive.bootstrap;

import com.rank.interactive.model.Player;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionDetails;
import com.rank.interactive.model.TransactionDetailsStatus;
import com.rank.interactive.repositories.PlayerRepository;
import com.rank.interactive.repositories.TransactionDetailsRepository;
import com.rank.interactive.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CasinoLoader implements CommandLineRunner {

    private  final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;

    public CasinoLoader(PlayerRepository playerRepository, TransactionRepository transactionRepository, TransactionDetailsRepository transactionDetailsRepository){
        this.playerRepository = playerRepository;
        this.transactionRepository = transactionRepository;
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        loadData();
    }

    private void loadData() {
        if(playerRepository.count() == 0){
            Player player1 = new Player();
            player1.setId(1L);
            player1.setUserName("Sethu");
            player1.setPassWord("Sethu123");
            player1.setEmailAddress("sethu@gmail.com");
            playerRepository.save(player1);
            System.out.println(player1.toString());


            if(transactionRepository.count() ==0){

                Transaction transaction1 = new Transaction();
                transaction1.setId(1L);
                transaction1.setPlayer(player1);
                transaction1.setBalance(2000d);
                transactionRepository.save(transaction1);
                System.out.println(transaction1.toString());

                if(transactionDetailsRepository.count() ==0){
                    TransactionDetails details = new TransactionDetails();
                    details.setId(1L);
                    details.setTransaction(transaction1.getId());
                    details.setPlayer(player1.getId());
                    details.setTransactionDetailsStatus(TransactionDetailsStatus.DEPOSITED);
                    details.setAmount(transaction1.getBalance());
                    transactionDetailsRepository.save(details);
                    System.out.println(details.toString());

                }
            }
        }
    }
}
