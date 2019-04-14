package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.exception.InvalidOrderTypeException;
import ua.com.epam.entity.exception.InvalidLimitFormatException;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.AuthorService;

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
    public ResponseEntity<?> getAuthor(@PathVariable long authorId) {
        AuthorGetDto author = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * Get array of Authors in JSON. Can apply different filters after '?'
     * Example: '?p1=v1&p2=v2.1,v2.2'; It is possible to set custom 'sortBy'
     * parameter, limit response array size and set order type.
     * All unsuitable parameters will produce fault
     *
     * @param params not required -> will be parsed to Map<String, String>
     * @param sortBy not required, by default 'authorId' -> String value
     * @param orderType not required, by default 'asc' -> String value
     * @param limit not required, by default '10' -> int value
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
     * Get array of Authors in JSON. Can sort by any other one json key.
     * If in JSON will not be such parameters will be thrown exception.
     * By default sort in ascending order. Descending order is available too.
     * Any others query params (expect 'sortBy' and 'order') will be ignored.
     *
     * @param sortBy not required, by default 'authorId' -> String value
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
}
