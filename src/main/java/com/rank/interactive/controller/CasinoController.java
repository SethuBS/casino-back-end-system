package com.rank.interactive.controller;

import com.rank.interactive.dto.request.TransactionsRequest;
import com.rank.interactive.dto.request.WagerRequest;
import com.rank.interactive.dto.request.WinRequest;
import com.rank.interactive.dto.response.BalanceResponse;
import com.rank.interactive.dto.response.TransactionResponse;
import com.rank.interactive.services.CasinoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${casino.api.base-path}")
public class CasinoController
{
    private final CasinoService casinoService;

    @GetMapping("${casino.api.balance-path}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable @Positive Long playerId)
    {
        return ResponseEntity.ok(new BalanceResponse(playerId, casinoService.getBalance(playerId)));
    }

    @PostMapping("${casino.api.wager-path}")
    public ResponseEntity<Void> processWager(@Valid @RequestBody WagerRequest request)
    {
        casinoService.processWager(
                request.transactionId(),
                request.playerId(),
                request.amount(),
                request.promotionCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("${casino.api.win-path}")
    public ResponseEntity<Void> processWin(@Valid @RequestBody WinRequest request)
    {
        casinoService.processWin(request.transactionId(), request.playerId(), request.amount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("${casino.api.transactions-path}")
    public ResponseEntity<List<TransactionResponse>> getLastTenTransactions(
            @Valid @RequestBody TransactionsRequest request)
    {
        return ResponseEntity.ok(casinoService.getRecentTransactions(request.username(), request.password())
                .stream()
                .map(TransactionResponse::from)
                .toList());
    }

}
