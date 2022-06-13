package com.tw.bootcamp.bookshop.book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void insertBooks(){
        bookRepository.deleteAll();
        Book harryPotter = new BookTestBuilder().withName("Harry Potter").withAuthorName("J K Rowling").build();
        Book alchemist = new BookTestBuilder().withName("Alchemist").withAuthorName("Paulo Cohelho").build();
        Book sherlockHolmes = new BookTestBuilder().withName("Sherlock Holmes").withAuthorName("Arthur Conan Doyale").withISBN("123IND22").build();
        bookRepository.saveAll(Arrays.asList(harryPotter,alchemist,sherlockHolmes));
    }

    @AfterEach
    void deleteBooks() {
        bookRepository.deleteAll();
    }

    @Test
    public void shouldFindAllBooksAndSortBYName(){
        assertEquals(3, bookRepository.findAllByOrderByNameAsc().size());
        assertEquals("Alchemist", bookRepository.findAllByOrderByNameAsc().get(0).getName());
    }

    @Disabled
    @Test
    public  void shouldReturnBookNamedHarryPotter(){
        assertEquals(1, bookRepository.searchBooks("Alchemist").size());
        assertEquals("Harry Potter", bookRepository.searchBooks("Alchemist").get(0).getName());
    }

    @Disabled
    @Test
    public  void shouldReturnHarryPottertBookWhenSearchedWithAuthorNameJKRowling(){
        assertEquals("Harry Potter", bookRepository.searchBooks("Rowling").get(0).getName());
    }

    @Test
    public  void shouldReturnSherlockHolmesWhenSearchedWithISBN(){
        assertEquals("Sherlock Holmes", bookRepository.findByISBN("123IND22").get(0).getName());
    }
}
