package com.assessment.seerbit.exception;

public class NoTransactionsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public NoTransactionsException(String s){
        super(s);
    }
}
