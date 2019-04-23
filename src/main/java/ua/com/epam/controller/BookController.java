package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.book.BookDto;
import ua.com.epam.entity.dto.book.BookWithAuthorAndGenreDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidOrderTypeException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library")
public class BookController {

    @Autowired
    private BookService bookService;

    private void checkOrdering(String orderType) {
        if (!orderType.equals("asc") && !orderType.equals("desc")) {
            throw new InvalidOrderTypeException(orderType);
        }
    }

    private void checkSortByKeyInGroup(String sortBy) {
        if (!JsonKeysConformity.ifJsonKeyExistsInGroup(sortBy, JsonKeysConformity.Group.BOOK)) {
            throw new NoSuchJsonKeyException(sortBy);
        }
    }

    /**
     * @param bookId
     * @return
     */
    @GetMapping(value = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBook(
            @PathVariable Long bookId) {
        BookDto response = bookService.findBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param bookId
     * @return
     */
    @GetMapping(value = "/book/{bookId}/author/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookWithAuthorAndGenre(
            @PathVariable Long bookId) {
        BookWithAuthorAndGenreDto response = bookService.findBookWithAuthorAndGenreInfos(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param sortBy
     * @param orderType
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/books/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooks(
            @RequestParam(name = "sortBy", defaultValue = "bookId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<BookDto> response = bookService.findAllBooksSortedPaginated(sortBy, orderType, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param genreId
     * @param sortBy
     * @param orderType
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooksInGenre(
            @PathVariable Long genreId,
            @RequestParam(name = "sortBy", defaultValue = "bookId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<BookDto> response = bookService.findBooksInGenre(genreId, sortBy, orderType, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     *
     * @param authorId
     * @param sortBy
     * @param orderType
     * @return
     */
    @GetMapping(value = "/author/{authorId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorBooks(
            @PathVariable Long authorId,
            @RequestParam(name = "sortBy", defaultValue = "bookId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<BookDto> response = bookService.findAuthorBooks(authorId, sortBy, orderType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     *
     * @param authorId
     * @param genreId
     * @return
     */
    @GetMapping(value = "/author/{authorId}/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorBooksInGenre(
            @PathVariable Long authorId,
            @PathVariable Long genreId) {
        List<BookDto> response = bookService.findBooksOfAuthorInGenre(authorId, genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     *
     * @param authorId
     * @param genreId
     * @param newBook
     * @return
     */
    @PostMapping(value = "/book/{authorId}/{genreId}/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewBook(
            @PathVariable Long authorId,
            @PathVariable Long genreId,
            @RequestBody @Valid BookDto newBook) {
        BookWithAuthorAndGenreDto response = bookService.addNewBook(authorId, genreId, newBook);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *
     *
     * @param bookId
     * @param updatedBook
     * @return
     */
    @PutMapping(value = "/book/{bookId}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookDto updatedBook) {
        BookDto response = bookService.updateExistedBook(bookId, updatedBook);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     *
     * @param bookId
     * @return
     */
    @DeleteMapping(value = "/book/{bookId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBook(
            @PathVariable Long bookId) {
        BookDto response = bookService.deleteExistedBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
