package com.assessment.seerbit;

import com.assessment.seerbit.controller.TransactionController;
import com.assessment.seerbit.exception.InvalidTransactionException;
import com.assessment.seerbit.payload.TransactionRequest;
import com.assessment.seerbit.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@SpringBootTest
@Slf4j
public class TransactionControllerTests {
    private TransactionService transactionService;
    private TransactionController transactionController;


    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);
    }

    @Test
    @DisplayName("Create transaction successfully")
    void createTransaction_ValidRequest_ReturnsCreated() throws InvalidTransactionException {

        TransactionRequest transactionRequest = new TransactionRequest("10.25", "2023-05-29T10:00:00Z");
        doNothing().when(transactionService).createTransaction(any(TransactionRequest.class));


        ResponseEntity<?> response = transactionController.createTransaction(transactionRequest);


        verify(transactionService, times(1)).createTransaction(transactionRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    @DisplayName("Create invalid transaction")
    void createTransaction_InvalidTransaction_ReturnsNoContent() throws InvalidTransactionException {

        TransactionRequest transactionRequest = new TransactionRequest("10.25", "invalid_timestamp");
        InvalidTransactionException exception = new InvalidTransactionException(false, "Invalid transaction");
        doThrow(exception).when(transactionService).createTransaction(any(TransactionRequest.class));

        ResponseEntity<?> response = transactionController.createTransaction(transactionRequest);


        verify(transactionService, times(1)).createTransaction(transactionRequest);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
