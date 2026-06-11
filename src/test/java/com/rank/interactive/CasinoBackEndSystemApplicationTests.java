package com.rank.interactive;

import com.rank.interactive.controller.CasinoController;
import com.rank.interactive.exceptions.PlayerNotFoundException;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.model.TransactionType;
import com.rank.interactive.services.CasinoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CasinoController.class,
        properties =
        {
                "casino.auth.transaction-history-password=test-password",
                "casino.promotion.free-wager-code=test-promotion"
        })
class CasinoBackEndSystemApplicationTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CasinoService casinoService;

    @Test
    void testGetBalance() throws Exception
    {
        Mockito.when(casinoService.getBalance(anyLong())).thenReturn(new BigDecimal("1000.00"));

        mockMvc.perform(MockMvcRequestBuilders.get("/casino/balance/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerId").value(1))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void testGetBalanceReturnsBadRequestWhenPlayerDoesNotExist() throws Exception
    {
        Mockito.when(casinoService.getBalance(99L)).thenThrow(new PlayerNotFoundException(99L));

        mockMvc.perform(MockMvcRequestBuilders.get("/casino/balance/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Player not found: 99"));
    }

    @Test
    void testGetBalanceRejectsInvalidPlayerId() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/casino/balance/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("must be greater than 0")));
    }

    @Test
    void testProcessWager() throws Exception
    {
        Mockito.doNothing().when(casinoService).processWager(anyString(), anyLong(), any(BigDecimal.class), anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/wager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx123\", \"playerId\": 1, \"amount\": 100, \"promotionCode\": \"test-promotion\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testProcessWagerRejectsInvalidRequest() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/casino/wager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx123\", \"playerId\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("amount must not be null"));
    }

    @Test
    void testProcessWagerRejectsAmountWithTooManyFractionDigits() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/casino/wager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx123\", \"playerId\": 1, \"amount\": 100.123}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("amount numeric value out of bounds")));
    }

    @Test
    void testProcessWin() throws Exception
    {
        Mockito.doNothing().when(casinoService).processWin(anyString(), anyLong(), any(BigDecimal.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/win")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx124\", \"playerId\": 1, \"amount\": 150}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLastTenTransactions() throws Exception
    {
        List<Transaction> transactions = List.of(Transaction.builder()
                .transactionId("tx125")
                .playerId(1L)
                .amount(new BigDecimal("50.00"))
                .type(TransactionType.WIN)
                .timestamp(LocalDateTime.parse("2026-06-11T08:30:00"))
                .build());
        Mockito.when(casinoService.getRecentTransactions(anyString(), anyString())).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"test-password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].transactionId").value("tx125"))
                .andExpect(jsonPath("$[0].type").value("win"));
    }

}
