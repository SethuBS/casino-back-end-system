package com.rank.interactive.Controllers;

import com.rank.interactive.model.Transaction;
import com.rank.interactive.services.CasinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/casino")
public class CasinoController {

    @Autowired
    private CasinoService casinoService;

    @GetMapping("/balance/{playerId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long playerId) {
        return ResponseEntity.ok(casinoService.getBalance(playerId));
    }

    @PostMapping("/wager")
    public ResponseEntity<Void> processWager(@RequestBody Map<String, Object> request) {
        String transactionId = (String) request.get("transactionId");
        Long playerId = ((Number) request.get("playerId")).longValue();
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String promotionCode = (String) request.get("promotionCode");

        casinoService.processWager(transactionId, playerId, amount, promotionCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/win")
    public ResponseEntity<Void> processWin(@RequestBody Map<String, Object> request) {
        String transactionId = (String) request.get("transactionId");
        Long playerId = ((Number) request.get("playerId")).longValue();
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        casinoService.processWin(transactionId, playerId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactions")
    public ResponseEntity<List<Transaction>> getLastTenTransactions(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        return ResponseEntity.ok(casinoService.getLastTenTransactions(username, password));
    }

}
