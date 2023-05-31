package com.assessment.seerbit.service;

import com.assessment.seerbit.data.Transaction;
import com.assessment.seerbit.exception.InvalidTransactionException;
import com.assessment.seerbit.exception.NoTransactionsException;
import com.assessment.seerbit.payload.StatisticsResponse;
import com.assessment.seerbit.payload.TransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private static final long THIRTY_SECONDS = 30 * 1000L;

    private static final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void createTransaction(TransactionRequest transactionRequest) throws InvalidTransactionException {
        BigDecimal amount = new BigDecimal(transactionRequest.getAmount());
        long timestamp = Instant.parse(transactionRequest.getTimestamp()).toEpochMilli();

        if (isFutureTransaction(timestamp)) {
            throw new InvalidTransactionException(true, "Transaction timestamp is in the future");
        }

        long currentTimestamp = Instant.now().toEpochMilli();
        if (!isRecentTransaction(timestamp, currentTimestamp)) {
            throw new InvalidTransactionException(false, "Transaction timestamp is too old");
        }

        Transaction transaction = new Transaction(amount, timestamp);
        transactions.put(timestamp, transaction);
        log.info("This is the transaction {}", transactions);
    }

    @Override
    public StatisticsResponse getTransactionStatistics() {
        long currentTimestamp = Instant.now().toEpochMilli();

        DoubleSummaryStatistics stats = transactions.values().stream()
                .filter(transaction -> isRecentTransaction(transaction.getTimestamp(), currentTimestamp))
                .map(Transaction::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics();

        if (stats.getCount() == 0) {
            throw new NoTransactionsException("No transactions available");
        }

        BigDecimal sum = BigDecimal.valueOf(stats.getSum()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal average = BigDecimal.valueOf(stats.getAverage()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal max = BigDecimal.valueOf(stats.getMax()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = BigDecimal.valueOf(stats.getMin()).setScale(2, RoundingMode.HALF_UP);
        long count = stats.getCount();

        return new StatisticsResponse(sum, average, max, min, count);
    }


    @Override
    public void deleteTransaction() {
        transactions.clear();
    }
    private boolean isFutureTransaction(long transactionTimestamp) {
        return transactionTimestamp > Instant.now().toEpochMilli();
    }

    private boolean isRecentTransaction(long transactionTimestamp, long currentTimestamp) {
        return (currentTimestamp - transactionTimestamp) <= THIRTY_SECONDS;
    }
}
