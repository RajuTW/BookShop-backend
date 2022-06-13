package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.purchase.InvalidBookException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldFetchAllBooks() {
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        List<Book> books = bookService.fetchAll();

        assertEquals(1, books.size());
        assertEquals("title", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll();

        assertEquals(2, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksNamedHarry() {
        Book HarryPotter1 = new BookTestBuilder().withName("HarryPotter and the goblet of fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(HarryPotter1);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.search("HarryPotter");

        assertEquals(1, books.size());
        assertEquals("HarryPotter and the goblet of fire", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksAuthorNamedRowling() {
        Book HarryPotter = new BookTestBuilder().withAuthorName("J K Rowling").build();
        Book PauloCoelho = new BookTestBuilder().withAuthorName("Paulo Coelho").build();
        bookRepository.save(HarryPotter);
        bookRepository.save(PauloCoelho);
        List<Book> books = bookService.search("Rowling");

        assertEquals(1, books.size());
        assertEquals("J K Rowling", books.get(0).getAuthorName());
    }

    @Test
    void shouldFetchAllBooksAuthorNamedLowerCase_rowling() {
        Book HarryPotter = new BookTestBuilder().withAuthorName("J K Rowling").build();
        Book PauloCoelho = new BookTestBuilder().withAuthorName("Paulo Coelho").build();
        bookRepository.save(HarryPotter);
        bookRepository.save(PauloCoelho);
        List<Book> books = bookService.search("rowling");

        assertEquals(1, books.size());
        assertEquals("J K Rowling", books.get(0).getAuthorName());
    }

    @Test
    void shouldUpdateInventoryWhilePersistingBooks() {
        Book HarryPotter = new BookTestBuilder().withAuthorName("J K Rowling").withISBN("ISBN1").withNumberOfAvailableBooks(2).build();
        Book PauloCoelho = new BookTestBuilder().withAuthorName("Paulo Coelho").withISBN("ISBN2").withNumberOfAvailableBooks(3).build();
        HarryPotter = bookRepository.save(HarryPotter);
        PauloCoelho = bookRepository.save(PauloCoelho);

        BookRequest newBookRequest = BookRequest.builder().authorName("J K Rowling").isbn("ISBN1").numberOfAvailableBooks(3).build();
        bookService.persistBooks(new ArrayList<BookRequest>() {{
            add(newBookRequest);
        }});
        Book book = bookService.getBookByISBN("ISBN1");

        assertEquals(5, book.getNumberOfAvailableBooks());
    }

    @Test
    void shouldReturnBookWhenBookIdIsValid() throws InvalidBookException {
        Book HarryPotter = new BookTestBuilder().withAuthorName("J K Rowling").withISBN("ISBN1").withNumberOfAvailableBooks(2).build();
        HarryPotter = bookRepository.save(HarryPotter);

        Book book = bookService.getBookById(HarryPotter.getId());
        assertEquals("ISBN1", book.getISBN());
    }

    @Test
    void shouldThrowInvalidBookExceptionWhenBookIdIsNotValid() throws Exception{
        Long inValidBookId = 12345L;

        assertThrows(InvalidBookException.class, () -> bookService.getBookById(inValidBookId));
    }

}