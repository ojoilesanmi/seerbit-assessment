package com.assessment.seerbit.service;

import com.assessment.seerbit.exception.InvalidTransactionException;
import com.assessment.seerbit.payload.StatisticsResponse;
import com.assessment.seerbit.payload.TransactionRequest;

public interface TransactionService {

    void createTransaction(TransactionRequest transactionRequest) throws InvalidTransactionException;
    StatisticsResponse getTransactionStatistics();
    void deleteTransaction();
}
