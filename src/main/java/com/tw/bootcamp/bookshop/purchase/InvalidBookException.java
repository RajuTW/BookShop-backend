package com.tw.bootcamp.bookshop.purchase;

public class InvalidBookException extends Exception {
    public InvalidBookException() {
        super("Book ID does not exist");
    }
}
