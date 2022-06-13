package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.purchase.InvalidBookException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/books")
@RestController
@CrossOrigin(origins = {"https://batch-07-team-c-ui.herokuapp.com", "http://localhost:3000"})
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping()
    @Operation(summary = "List all books or search books if query param is passed", description = "List all books or search books if query param is passed", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> list(@RequestParam(name = "query", required = false) String searchQuery) {

        if (searchQuery == null || searchQuery.isEmpty()) {
            return bookService.fetchAll().stream()
                    .map(Book::toResponse)
                    .collect(Collectors.toList());
        }
        return bookService.search(searchQuery).stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());

    }

    @PostMapping()
    @Operation(summary = "save books", description = "save the list of books", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "save books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    String persistBooks(@RequestBody List<BookRequest> inputBooks) {
        return bookService.persistBooks(inputBooks);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Return book details", description = "Return book details if book id is passed", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Book Details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))}),
            @ApiResponse(responseCode = "422", description = "Request isn't valid or process has failed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    BookResponse getBookDetails(@PathVariable Long id) throws InvalidBookException {

        return bookService.getBookById(id).toResponse();

    }

}


