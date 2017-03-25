package com.sjsu.model;


public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException(String s) {
        super(s);
    }
}
