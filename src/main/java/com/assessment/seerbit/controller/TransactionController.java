package com.assessment.seerbit.controller;

import com.assessment.seerbit.exception.InvalidTransactionException;
import com.assessment.seerbit.exception.NoTransactionsException;
import com.assessment.seerbit.payload.StatisticsResponse;
import com.assessment.seerbit.payload.TransactionRequest;
import com.assessment.seerbit.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.createTransaction(transactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidTransactionException e) {
            if (e.isFutureTransaction()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> getTransactionStatistics() {
        try {
            StatisticsResponse response = transactionService.getTransactionStatistics();
            return ResponseEntity.ok(response);
        } catch (NoTransactionsException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new StatisticsResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<?> deleteTransactions() {
        transactionService.deleteTransaction();
        return ResponseEntity.noContent().build();
    }
}
