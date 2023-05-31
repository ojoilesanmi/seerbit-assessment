package com.assessment.seerbit;

import com.assessment.seerbit.exception.InvalidTransactionException;
import com.assessment.seerbit.payload.StatisticsResponse;
import com.assessment.seerbit.payload.TransactionRequest;
import com.assessment.seerbit.service.TransactionService;
import com.assessment.seerbit.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    @DisplayName("Create a transaction with a future timestamp throws InvalidTransactionException")
    void createTransaction_FutureTransaction_ThrowsInvalidTransactionException() {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("120.00");
        transactionRequest.setTimestamp(Instant.now().plusSeconds(60).toString());

        assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
    }


    @Test
    @DisplayName("Create a valid transaction")
    void createTransaction_ValidTransaction_Success() {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("120.00");
        transactionRequest.setTimestamp(Instant.now().toString());

        assertDoesNotThrow(() -> {
            transactionService.createTransaction(transactionRequest);
        });
    }

    @Test
    @DisplayName("Create a transaction with a future timestamp throws InvalidTransactionException")
    void createTransaction_FutureTimestamp_ThrowsInvalidTransactionException() {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("100.00");
        transactionRequest.setTimestamp(Instant.now().plus(Duration.ofDays(1)).toString());

        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
        assertTrue(exception.isFutureTransaction());
        assertEquals("Transaction timestamp is in the future", exception.getMessage());
    }

    @Test
    @DisplayName("Create a transaction with an old timestamp throws InvalidTransactionException")
    void createTransaction_OldTimestamp_ThrowsInvalidTransactionException() {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("80.00");
        transactionRequest.setTimestamp(Instant.now().minus(Duration.ofDays(10)).toString());

        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
        assertFalse(exception.isFutureTransaction());
        assertEquals("Transaction timestamp is too old", exception.getMessage());
    }

    @Test
    @DisplayName("Create a transaction with an invalid amount format throws NumberFormatException")
    void createTransaction_InvalidAmountFormat_ThrowsNumberFormatException() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("abc");
        transactionRequest.setTimestamp(Instant.now().toString());

        assertThrows(NumberFormatException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
    }

    @Test
    @DisplayName("Get transaction statistics for recent transactions")
    void getTransactionStatistics_RecentTransactions_ReturnsCorrectStatistics() throws InvalidTransactionException {

        transactionService.deleteTransaction();
        TransactionRequest recentTransactionRequest1 = new TransactionRequest("10.25", Instant.now().minusSeconds(20).toString());
        TransactionRequest recentTransactionRequest2 = new TransactionRequest("20.50", Instant.now().minusSeconds(15).toString());
        TransactionRequest recentTransactionRequest3 = new TransactionRequest("15.75", Instant.now().minusSeconds(5).toString());

        transactionService.createTransaction(recentTransactionRequest1);
        transactionService.createTransaction(recentTransactionRequest2);
        transactionService.createTransaction(recentTransactionRequest3);

        StatisticsResponse statistics = transactionService.getTransactionStatistics();

        assertEquals(new BigDecimal("46.50").setScale(2), statistics.getSum());
        assertEquals(new BigDecimal("15.50").setScale(2), statistics.getAvg());
        assertEquals(new BigDecimal("20.50").setScale(2), statistics.getMax());
        assertEquals(new BigDecimal("10.25").setScale(2), statistics.getMin());
        assertEquals(3, statistics.getCount());
    }
}
