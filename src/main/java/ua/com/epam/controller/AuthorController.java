package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorGetDto;
import ua.com.epam.entity.exception.InvalidOrderTypeException;
import ua.com.epam.service.AuthorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * Get Author entity by authorId
     *
     * @param authorId -> long value
     * @return -> Response with Author Json Dto or 404 Not Found
     */
    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @PathVariable long authorId,
            BindingResult result) {
        AuthorGetDto author = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     *
     * @param params not required -> will be parsed to Map<String, String>
     * @param sortBy not required, by default 'authorId' -> String value
     * @param order not required, by default 'asc' -> String value
     * @return Response with array of Authors, empty array or 400 - Bed Request
     */
    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorsByParams(
            @RequestParam Map<String, String> params,
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        checkOrdering(order);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * Return array of Authors in JSON. Can sort by any other one json key.
     * If in JSON will not be such parameters will be thrown exception.
     * By default sort in ascending order. Descending order is available too.
     * Any others query params (expect 'sortBy' and 'order') will be ignored.
     *
     * @param sortBy not required, by default 'authorId' -> String value
     * @param order not required, by default 'asc' -> String value
     * @return -> ResponseEntity with array of authors or 400 - Bad Request
     */
    @GetMapping(value = "/authors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        checkOrdering(order);
        List<AuthorGetDto> authors = authorService.getAllAuthorsSortedBy(sortBy, order);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    private void checkOrdering(String orderType) {
        if (!orderType.equals("asc") && !orderType.equals("desc")) {
            throw new InvalidOrderTypeException(orderType);
        }
    }
}
