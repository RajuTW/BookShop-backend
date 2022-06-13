package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.purchase.InvalidBookException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll() {
        return bookRepository.findAllByOrderByNameAsc();
    }

    public String persistBooks(List<BookRequest> bookRequests) {
        List<Book> books = bookRequests.stream().map(bookRequest ->  Book.create(bookRequest)).collect(Collectors.toList());
        int updatedBookCount = 0;
        int insertedBookCount = 0;
        for(Book b: books){
            List<Book> listBooks = bookRepository.findByISBN(b.getISBN());
            if(listBooks !=null && !listBooks.isEmpty()) {
                Book dbBook = listBooks.get(0);
                if (dbBook != null) {
                    updatedBookCount++;
                    dbBook.setNumberOfAvailableBooks(dbBook.getNumberOfAvailableBooks() + b.getNumberOfAvailableBooks());
                    bookRepository.save(dbBook);
                }
            } else {
                insertedBookCount++;
                bookRepository.save(b);
            }
        }
        return  "Update " + updatedBookCount + " Books & Inserted " + insertedBookCount + " Books";
    }

    public List<Book> search(String searchQuery) {
        return bookRepository.searchBooks(searchQuery.toUpperCase());
    }

    public Book getBookByISBN(String isbn) {
        return bookRepository.findByISBN(isbn).get(0);
    }

    public Book getBookById(Long id) throws InvalidBookException {
        Optional<Book> book = bookRepository.findById(id);
        if(!book.isPresent()) {
            throw new InvalidBookException();
        }
        return book.get();
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}
