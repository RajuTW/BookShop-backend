package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.purchase.InvalidBookException;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldListAllBooksWhenPresent() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        when(bookService.fetchAll()).thenReturn(books);

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
        when(bookService.fetchAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchAll();
    }

    @Test
    void shouldPersistBooksWhenListOfBooksIsProvided() throws Exception {

        List<BookRequest> books = new ArrayList<BookRequest>();
        books.add(new BookRequest( "Java", "Richards",  "USD", 100, "ISBN1", 2));
        String resp = "Updated 10 Books & Inserted 1 Books";
        when(bookService.persistBooks(books)).thenReturn(resp);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .content(objectMapper.writeValueAsString(books))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(resp));

        verify(bookService, times(1)).persistBooks(books);
    }

    @Test
    void shouldReturnAuthorOrBookNamedHarry() throws Exception {
        when(bookService.search("Harry")).thenReturn(Arrays.asList(new BookTestBuilder().withName("Harry Potter and Goblet of fire").build()));

        mockMvc.perform(get("/books?query=Harry")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).search("Harry");
    }

    @Test
    void shouldReturnBookWhenBookIdIsValid() throws Exception {
        Book HarryPotter = new BookTestBuilder().withAuthorName("J K Rowling").withISBN("ISBN1").withNumberOfAvailableBooks(2).build();
        Long id = Long.valueOf(123);
        when(bookService.getBookById(id)).thenReturn(HarryPotter);
        mockMvc.perform(get("/books/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("authorName").value("J K Rowling"));
        verify(bookService, times(1)).getBookById(id);
    }

    @Test
    void shouldThrowExceptionWhenBookIdIsNotValid() throws Exception {
        Long id = Long.valueOf(123);
        when(bookService.getBookById(id)).thenThrow(new InvalidBookException());
        mockMvc.perform(get("/books/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Book ID does not exist"));
        verify(bookService, times(1)).getBookById(id);
    }
}