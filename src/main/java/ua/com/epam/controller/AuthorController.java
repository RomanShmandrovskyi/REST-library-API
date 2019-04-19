package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithBooksDto;
import ua.com.epam.entity.dto.author.SimpleAuthorWithGenresDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidOrderTypeException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.AuthorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    private void checkOrdering(String orderType) {
        if (!orderType.equals("asc") && !orderType.equals("desc")) {
            throw new InvalidOrderTypeException(orderType);
        }
    }

    private void checkSortByKeyInGroup(String sortBy) {
        if (!JsonKeysConformity.ifJsonKeyExistsInGroup(sortBy, JsonKeysConformity.Group.AUTHOR)) {
            throw new NoSuchJsonKeyException(sortBy);
        }
    }

    /**
     * Get Author entity by authorId
     *
     * @param authorId -> long value
     * @return -> Response with Author Json Dto or 404 - Author Not Found
     */
    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @PathVariable Long authorId) {
        AuthorDto author = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * Get array of Authors in JSON. Can apply different filters using query
     * parameters after '?'. Example: '?p1=v1&p2=v2.1,v2.2'. It is possible
     * to set custom 'sortBy' parameter, but only one per one request, limit
     * response array size and set order type (ascending or descending). All
     * unsuitable parameters will produce a fault.
     *
     * @param params    not required -> will be parsed to Map<String, String>
     * @param sortBy    not required, by default 'authorId' -> String value
     * @param orderType not required, by default 'asc' -> String value
     * @param limit     not required, by default '10' -> int value
     * @return -> Response with array of Authors, empty array or 400 - Bed Request
     */
    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorsByParams(
            @RequestParam Map<String, String> params,
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        params.putIfAbsent("sortBy", sortBy);
        params.putIfAbsent("orderType", orderType);
        params.putIfAbsent("limit", String.valueOf(limit));

        List<AuthorDto> response = authorService.filterAuthors(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of all existed Authors in JSON. Can sort by any other one
     * json key. If key not exist in JSON will be thrown exception. By
     * default sort in ascending order. Descending order is available too.
     * Any others query params (expect 'sortBy' and 'order') will be ignored.
     *
     * @param sortBy    not required, by default 'authorId' -> String value
     * @param orderType not required, by default 'asc' -> String value
     * @return -> ResponseEntity with array of authors or 400 - Bad Request
     */
    @GetMapping(value = "/authors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<AuthorDto> response = authorService.getAllAuthorsSortedBy(sortBy, orderType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Find all Genres of Author. Will return Simple info about Author with
     * array of Simple Genre objects with next values: 'genreId' and 'genreName'.
     *
     * @param authorId required -> long value
     * @return -> ResponseEntity with array of genres or 404 - Author Not Found
     */
    @GetMapping(value = "/author/{authorId}/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorGenres(
            @PathVariable Long authorId) {
        SimpleAuthorWithGenresDto response = authorService.getAuthorWithAllItGenres(authorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Find all Books of Author. Will return Simple info about Author with
     * array of Book objects with next values: 'bookId', 'bookName' and 'bookDescription'.
     *
     * @param authorId required -> long value
     * @return -> ResponseEntity with array of books or 404 - Author Not Found
     */
    @GetMapping(value = "/author/{authorId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorBooks(
            @PathVariable Long authorId) {
        SimpleAuthorWithBooksDto response = authorService.getAuthorWithAllItBooks(authorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new Author. Fields: 'authorId', 'authorName.first', 'authorName.second'
     * are mandatory. If field is skipped in JSON body it will assign empty string
     * for String type values and null for Date type.
     *
     * @param postAuthor required -> JSON body with new Author object
     * @return -> Response with new Author or 409 - Conflict
     */
    @PostMapping(value = "/author/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewAuthor(
            @RequestBody @Valid AuthorDto postAuthor) {
        AuthorDto response = authorService.addNewAuthor(postAuthor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existed Author. Consume full object with updated JSON fields.
     * Path param 'authorId' must be the same as in body to update. In other
     * way will be thrown exception.
     *
     * @param authorId      required -> long value
     * @param updatedAuthor -> JSON body with Author object to update
     * @return -> Response with updated Author or 404 - Not Found
     */
    @PutMapping(value = "/author/{authorId}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long authorId,
            @RequestBody @Valid AuthorDto updatedAuthor) {
        AuthorDto response = authorService.updateExistedAuthor(authorId, updatedAuthor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete existed Author by authorId. If Author with such authorId doesn't exist
     * it will produce 404 - Not Found. If Author has some related Books and 'forcibly'
     * is 'false' you will be informed, that Author has some Books. If 'forcibly'
     * indicator defining as 'true' it will delete Author and all related Books.
     *
     * @param authorId required -> long value
     * @param forcibly not required, by default 'false' -> boolean value
     * @return -> deleted Author object
     */
    @DeleteMapping(value = "/author/{authorId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAuthor(
            @PathVariable Long authorId,
            @RequestParam(name = "forcibly", defaultValue = "false") boolean forcibly) {
        AuthorDto response = authorService.deleteExistedAuthor(authorId, forcibly);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
