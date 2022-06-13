package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>  {
    List<Book> findAllByOrderByNameAsc();
    @Query(value =
            "select * from books where upper(name) like  %:searchQuery% OR upper(author_name) like %:searchQuery% ",
            //"select name,amount,author_name from books where to_tsvector(name || ' ' || author_name) @@to_tsquery(:searchQuery)",
            nativeQuery = true)
    List<Book> searchBooks(@Param("searchQuery") String searchQuery);

    List<Book> findByISBN(String isbn);
}
