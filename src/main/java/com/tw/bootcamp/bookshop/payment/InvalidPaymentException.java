package com.tw.bootcamp.bookshop.payment;

public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String str) {
        super(str);
    }
}
