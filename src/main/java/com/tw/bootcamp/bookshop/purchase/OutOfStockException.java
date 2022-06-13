package com.tw.bootcamp.bookshop.purchase;

public class OutOfStockException extends Exception {
    public OutOfStockException() {
        super("Book is out of Stock");
    }
}
