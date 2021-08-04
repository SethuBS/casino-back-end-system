package com.rank.interactive.services;

import com.rank.interactive.model.Player;
import org.springframework.stereotype.Component;

/**
 * Created by Sethu on 2021/08/04.
 */

@Component
public interface MoneyService {

    Double getBalance(Player player);
    Double deduct(Player player, Double amount);
    Double deposit(Player player, Double amount);
}
