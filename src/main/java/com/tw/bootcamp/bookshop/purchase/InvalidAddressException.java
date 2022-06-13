package com.tw.bootcamp.bookshop.purchase;

public class InvalidAddressException extends Exception {
    public InvalidAddressException() {
        super("Address ID does not exist");
    }
}
