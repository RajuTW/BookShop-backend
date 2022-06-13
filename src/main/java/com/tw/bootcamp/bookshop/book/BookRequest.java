package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.Column;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class BookRequest {
    private String name;
    private String authorName;
    private String currency;
    private double amount;
    private String isbn;
    private int numberOfAvailableBooks;
}
