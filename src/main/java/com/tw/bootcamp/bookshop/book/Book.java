package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String authorName;

    private String ISBN;

    private Integer numberOfAvailableBooks;
    @Embedded
    private Money price;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(price)
                .numberOfAvailableBooks(numberOfAvailableBooks)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ISBN.equals(book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }

    public static Book create(BookRequest bookRequest){
        return Book.builder()
                .name(bookRequest.getName())
                .authorName(bookRequest.getAuthorName())
                .ISBN((bookRequest.getIsbn()))
                .numberOfAvailableBooks(bookRequest.getNumberOfAvailableBooks())
                .price(new Money(bookRequest.getCurrency(), bookRequest.getAmount()))
                .build();
    }
}
