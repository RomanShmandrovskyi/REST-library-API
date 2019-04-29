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
import ua.com.epam.entity.exception.type.InvalidPageValueException;
import ua.com.epam.entity.exception.type.InvalidSizeValueException;
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

    private void checkPaginateParams(int page, int size) {
        if (page <= 0) {
            throw new InvalidPageValueException();
        }
        if (size < 0) {
            throw new InvalidSizeValueException();
        }
    }

    /**
     * Get Book entity by bookId.
     *
     * @param bookId required -> Long value.
     * @return -> ResponseEntity with:
     *            Book object |
     *            404 - Book Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBook(
            @PathVariable Long bookId) {
        BookDto response = bookService.findBookByBookId(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get Book entity with simple info about it genre and author.
     *
     * @param bookId required -> Long value.
     * @return -> ResponseEntity with:
     *            Special Book object |
     *            404 - Book Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/book/{bookId}/author/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookWithAuthorAndGenre(
            @PathVariable Long bookId) {
        BookWithAuthorAndGenreDto response = bookService.findBookWithAuthorAndGenreInfo(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of existed Book Objects. Can sort by any other one json key.
     * If key not exists in JSON, will be thrown exception. By default sort in
     * ascending order. Descending order is available too.
     * <p>
     * This endpoint can also paginate response: just set page number to 'page'
     * param and needed entities count on one page in 'size' param. By default
     * pagination is enabled, but you can disable it: just set 'pagination'
     * parameter to 'false'. In this case, you get all existed Book objects
     * from DB.
     *
     * @param sortBy    not required, by default 'bookId' -> String value.
     * @param orderType not required, by default 'asc' -> String value.
     * @param page      not required, by default '1' -> Integer value.
     * @param size      not required, by default '10' -> Integer value.
     * @return -> ResponseEntity with:
     *            array of Books |
     *            empty array |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooks(
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "bookId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<BookDto> response = bookService.findAllBooks(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of existed Book Objects in some Genre. Can sort by any other one
     * json key. If key not exists in JSON, will be thrown exception. By default
     * sort in ascending order. Descending order is available too.
     * <p>
     * This endpoint can also paginate response: just set page number to 'page'
     * param and needed entities count on one page in 'size' param. By default
     * pagination is enabled, but you can disable it: just set 'pagination'
     * parameter to 'false'. In this case, you get all existed Book objects
     * in special Genre from DB.
     * <p>
     * Any others query params expect 'sortBy', 'orderType', 'page', 'size'
     * and 'pagination' will be ignored.
     *
     * @param genreId    required -> Long value
     * @param pagination not required, by default 'true' -> Boolean value.
     * @param sortBy     not required, by default 'bookId' -> String value.
     * @param orderType  not required, by default 'asc' -> String value.
     * @param page       not required, by default '1' -> Integer value.
     * @param size       not required, by default '10' -> Integer value.
     * @return -> ResponseEntity with:
     *            array of Books |
     *            empty array |
     *            404 - Genre Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooksInGenre(
            @PathVariable Long genreId,
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "bookId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<BookDto> response = bookService.findBooksInGenre(genreId, sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of existed Book Objects of some Author. Can sort by any other one
     * json key. If key not exists in JSON, will be thrown exception. By default
     * sort in ascending order. Descending order is available too. Any others query
     * params (expect 'sortBy', 'orderType') will be ignored.
     * <p>
     * Any others query params expect 'sortBy' and 'orderType' will be ignored.
     *
     * @param authorId  required -> Long value
     * @param sortBy    not required, by default 'bookId' -> String value.
     * @param orderType not required, by default 'asc' -> String value.
     * @return -> ResponseEntity with:
     *            array of books |
     *            empty array |
     *            404 - Author Not Found |
     *            400 - Bad Request.
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
     * Get array of existed Book Objects of some Author in some Genre.
     *
     * @param authorId required -> Long value.
     * @param genreId  required -> Long value.
     * @return -> ResponseEntity with:
     *            array of books |
     *            empty array |
     *            404 - Author Not Found |
     *            404 - Genre Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/author/{authorId}/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorBooksInGenre(
            @PathVariable Long authorId,
            @PathVariable Long genreId) {
        List<BookDto> response = bookService.findBooksOfAuthorInGenre(authorId, genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new Book. Fields: 'bookId', 'bookName' and 'bookLanguage' are
     * mandatory. To add new Book it is also mandatory set it author (by
     * authorId) and genre (by genreId) in path variables. If some not mandatory
     * fields are skipped in JSON body it will assign empty string for String
     * type values, '0' for Integer type values and '0.0' for Double type values.
     *
     * @param authorId required -> Long value
     * @param genreId  required -> Long value
     * @param newBook  required -> JSON body with new Book object
     * @return -> ResponseEntity with:
     *            created Author object |
     *            409 - Conflict |
     *            400 - Bad Request.
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
     * Update existed Book. Consume full object with needed updated JSON
     * fields. Path param 'bookId' must be the same as in body to update.
     * In other way will be thrown exception.
     *
     * @param bookId      required -> Long value.
     * @param updatedBook not required -> JSON body with Book object to update
     * @return -> ResponseEntity with:
     *            updated Author object |
     *            404 - Book Not Found |
     *            400 - Bad Request.
     */
    @PutMapping(value = "/book/{bookId}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookDto updatedBook) {
        BookDto response = bookService.updateExistedBook(bookId, updatedBook);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete existed Book by 'bookId'. If Book with such 'bookId' doesn't
     * exist it will produce 404 - Book Not Found.
     *
     * @param bookId required -> Long value.
     * @return -> ResponseEntity with:
     *            deleted Book object |
     *            404 - Book Not Found |
     *            400 - Bad Request.
     */
    @DeleteMapping(value = "/book/{bookId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBook(
            @PathVariable Long bookId) {
        BookDto response = bookService.deleteExistedBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}