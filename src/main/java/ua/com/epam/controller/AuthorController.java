package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidLimitFormatException;
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

    private void checkLimit(String limit) {
        try {
            Integer.parseInt(limit);
        } catch (NumberFormatException nfe) {
            throw new InvalidLimitFormatException();
        }
    }

    /**
     * Get Author entity by authorId
     *
     * @param authorId -> long value
     * @return -> Response with Author Json Dto or 404 - Not Found
     */
    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @PathVariable long authorId) {
        AuthorGetDto author = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * Get array of Authors in JSON. Can apply different filters after '?'
     * Example: '?p1=v1&p2=v2.1,v2.2'; It is possible to set custom 'sortBy'
     * parameter, limit response array size and set order type.
     * All unsuitable parameters will produce a fault
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
            @RequestParam(name = "limit", defaultValue = "10") String limit) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        params.putIfAbsent("sortBy", sortBy);
        params.putIfAbsent("orderType", orderType);
        params.putIfAbsent("limit", limit);

        checkLimit(params.get("limit"));

        List<AuthorGetDto> authorsFiltered = authorService.filterAuthors(params);
        return new ResponseEntity<>(authorsFiltered, HttpStatus.OK);
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
        List<AuthorGetDto> authors = authorService.getAllAuthorsSortedBy(sortBy, orderType);
        return new ResponseEntity<>(authors, HttpStatus.OK);
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
        AuthorGetDto response = authorService.addNewAuthor(postAuthor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existed Author. Consume full object with updated JSON fields.
     * Path param 'authorId' must be the same as in body to update. In other way
     * will be thrown exception.
     *
     * @param authorId      -> long value
     * @param updatedAuthor -> JSON body with Author object to update
     * @return -> Response with updated Author or 404 - Not Found
     */
    @PutMapping(value = "/author/{authorId}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @PathVariable long authorId,
            @RequestBody @Valid AuthorDto updatedAuthor) {
        AuthorGetDto response = authorService.updateExistedAuthor(authorId, updatedAuthor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

/*    @DeleteMapping(value = "/author/{authorId}/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAuthor(
            @PathVariable long authorId,
            @RequestParam(name = "forcibly", defaultValue = "false") boolean forcibly) {

    }*/
}
