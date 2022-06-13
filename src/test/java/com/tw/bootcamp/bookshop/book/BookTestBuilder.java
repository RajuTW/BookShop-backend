package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;

public class BookTestBuilder {
    private final Book.BookBuilder bookBuilder;

    public BookTestBuilder() {
        bookBuilder = Book.builder().name("Harry Potter")
                .authorName("J K Rowling")
                .price(Money.rupees(300))
                .numberOfAvailableBooks(3);
    }

    public Book build() {
        return bookBuilder.build();
    }

    public BookTestBuilder withPrice(int price) {
        bookBuilder.price(Money.rupees(price));
        return this;
    }

    public BookTestBuilder withName(String name) {
        bookBuilder.name(name);
        return this;
    }

    public BookTestBuilder withAuthorName(String authorName) {
        bookBuilder.authorName(authorName);
        return this;
    }

    public BookTestBuilder withISBN(String ISBN) {
        bookBuilder.ISBN(ISBN);
        return this;
    }

    public BookTestBuilder withNumberOfAvailableBooks(int numberOfAvailableBooks) {
        bookBuilder.numberOfAvailableBooks(numberOfAvailableBooks);
        return this;
    }
}
