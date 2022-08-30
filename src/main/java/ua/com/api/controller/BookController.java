package ua.com.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.api.entity.dto.book.BookDto;
import ua.com.api.exception.model.ExceptionResponse;
import ua.com.api.service.BookService;
import ua.com.api.service.util.annotation.AllowableValues;

import javax.validation.constraints.Min;
import java.util.List;

import static ua.com.api.service.constants.Constants.*;

@RestController
@RequestMapping("${server.base.url}")
@Validated

@Tag(name = "Book", description = "Book table endpoints")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "get Book by 'bookId'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Special Book object in JSON",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBook(
            @Parameter(description = "existed Book ID", required = true)
            @PathVariable
            Long bookId) {
        BookDto response = bookService.findBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "get all Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Book objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooks(
            @Parameter(description = "paginate response")
            @RequestParam(name = PAGINATION, defaultValue = TRUE)
            Boolean pagination,

            @Parameter(description = "page number")
            @RequestParam(name = PAGE, defaultValue = "1")
            @Min(value = 1, message = "Value of 'page' parameter must be positive and greater than zero!")
            Integer page,

            @Parameter(description = "count of objects per one page")
            @RequestParam(name = SIZE, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = "Value of 'size' parameter must be positive and greater than zero!")
            Integer size,

            @Parameter(description = "custom sort parameter, can also assume 'square' and 'volume' parameters")
            @RequestParam(name = SORT_BY, defaultValue = NAME)
            String sortBy,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of 'orderType' parameter must be '" + ASC + "' or '"+ DESC + "'")
            String orderType) {
        List<BookDto> response = bookService.findAllBooks(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "get all Books in special Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Book objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooksInGenre(
            @Parameter(description = "existed Genre ID", required = true)
            @PathVariable
            Long genreId,

            @Parameter(description = "paginate response")
            @RequestParam(name = PAGINATION, defaultValue = TRUE)
            Boolean pagination,

            @Parameter(description = "page number")
            @RequestParam(name = PAGE, defaultValue = "1")
            @Min(value = 1, message = "Value of 'page' parameter must be positive and greater than zero!")
            Integer page,

            @Parameter(description = "count of objects per one page")
            @RequestParam(name = SIZE, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = "Value of 'size' parameter must be positive and greater than zero!")
            Integer size,

            @Schema(example = NAME)
            @Parameter(description = "custom sort parameter")
            @RequestParam(name = SORT_BY, defaultValue = NAME)
            String sortBy,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of 'orderType' parameter must be '" + ASC + "' or '"+ DESC + "'")
            String orderType) {

        List<BookDto> response = bookService.findBooksInGenre(genreId, sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "get all Books of special Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Book objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/author/{authorId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorBooks(
            @Parameter(description = "existed Author ID", required = true)
            @PathVariable
            Long authorId,

            @Parameter(description = "custom sort parameter")
            @RequestParam(name = SORT_BY, defaultValue = NAME)
            String sortBy,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of 'orderType' parameter must be '" + ASC + "' or '"+ DESC + "'")
            String orderType) {

        List<BookDto> response = bookService.findAuthorBooks(authorId, sortBy, orderType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "get all Books of special Author in special Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Book objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author or Genre not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/author/{authorId}/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorBooksInGenre(
            @Parameter(description = "existed Author ID", required = true)
            @PathVariable
            Long authorId,

            @Parameter(description = "existed Genre ID", required = true)
            @PathVariable
            Long genreId) {
        List<BookDto> response = bookService.findBooksOfAuthorInGenre(authorId, genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "search for books by book name, return first 5 the most relevant results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Book objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/books/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchForExistedBooks(
            @Parameter(description = "Searched query. At least 5 symbols exclude spaces in each word.", required = true)
            @RequestParam(name = QUERY)
            String query) {
        List<BookDto> response = bookService.searchForExistedBooks(query);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "create new Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "newly created Book",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Book with such id already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/book/{authorId}/{genreId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewBook(
            @Parameter(description = "existed Author ID", required = true)
            @PathVariable
            Long authorId,

            @Parameter(description = "existed Genre ID", required = true)
            @PathVariable
            Long genreId,

            @Parameter(description = "Book to add", name = "Book object", required = true)
            @RequestBody @Validated
            BookDto newBook) {
        BookDto response = bookService.addNewBook(authorId, genreId, newBook);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "update existed Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated Book object",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book to update not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(
            @Parameter(description = "Book to update", name = "Book object", required = true)
            @RequestBody @Validated BookDto updatedBook) {
        BookDto response = bookService.updateExistedBook(updatedBook);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "delete existed Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book to delete not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "existed Book ID", required = true)
            @PathVariable Long bookId) {
        bookService.deleteExistedBook(bookId);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}