package com.rank.interactive;


import com.rank.interactive.Controllers.CasinoController;
import com.rank.interactive.model.Transaction;
import com.rank.interactive.services.CasinoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CasinoController.class)
class CasinoBackEndSystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CasinoService casinoService;

    CasinoBackEndSystemApplicationTests(MockMvc mockMvc, CasinoService casinoService) {
        this.mockMvc = mockMvc;
		this.casinoService = casinoService;
    }

    @Test
    void testGetBalance() throws Exception {
        Mockito.when(casinoService.getBalance(anyLong())).thenReturn(new BigDecimal("1000.00"));

        mockMvc.perform(MockMvcRequestBuilders.get("/casino/balance/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1000.00"));
    }

    @Test
    void testProcessWager() throws Exception {
        Mockito.doNothing().when(casinoService).processWager(anyString(), anyLong(), any(BigDecimal.class), anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/wager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx123\", \"playerId\": 1, \"amount\": 100, \"promotionCode\": \"paper\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testProcessWin() throws Exception {
        Mockito.doNothing().when(casinoService).processWin(anyString(), anyLong(), any(BigDecimal.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/win")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\": \"tx124\", \"playerId\": 1, \"amount\": 150}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLastTenTransactions() throws Exception {
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        Mockito.when(casinoService.getLastTenTransactions(anyString(), anyString())).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.post("/casino/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"swordfish\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
