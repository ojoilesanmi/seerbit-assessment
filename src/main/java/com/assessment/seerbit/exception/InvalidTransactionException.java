package com.assessment.seerbit.exception;

public class InvalidTransactionException extends Exception {
//    private  final boolean futureTransaction;
//
//    public InvalidTransactionException(boolean futureTransaction) {
//        this.futureTransaction = futureTransaction;
//    }
//
//    public boolean isFutureTransaction() {
//        return futureTransaction;
//    }

    private final boolean futureTransaction;

    public InvalidTransactionException(boolean futureTransaction) {
        this.futureTransaction = futureTransaction;
    }

    public InvalidTransactionException(boolean futureTransaction, String message) {
        super(message);
        this.futureTransaction = futureTransaction;
    }

    public boolean isFutureTransaction() {
        return futureTransaction;
    }
}
