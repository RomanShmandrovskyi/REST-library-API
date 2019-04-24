package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidOrderTypeException;
import ua.com.epam.entity.exception.type.InvalidPageValueException;
import ua.com.epam.entity.exception.type.InvalidSizeValueException;
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

    private void checkPaginateParams(int page, int size) {
        if (page <= 0) {
            throw new InvalidPageValueException();
        }
        if (size < 0) {
            throw new InvalidSizeValueException();
        }
    }

    /**
     * Get Author entity by authorId.
     *
     * @param authorId -> Long value
     * @return -> ResponseEntity with:
     *            Author object |
     *            404 - Author Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/author/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @PathVariable Long authorId) {
        AuthorDto response = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of filtered Author objects. Can apply different filters using query
     * parameters after '?'. Example: '?p1=v1&p2=v2.1,v2.2'. Assume as one value for
     * parameter, so several, just join it using ','. It is possible to set one
     * custom 'sortBy' parameter and order type ('asc' or 'desc'). All unsuitable
     * parameters that not exist in JSON, will produce a fault.
     * <p>
     * This endpoint can also paginate response: just set page number to 'page' param
     * and needed entities count on one page in 'size' param. By default pagination
     * is enabled, but you can disable it. In this case, if there aren't any filter
     * parameters, you get all existed Authors from DB.
     *
     * @param params     not required -> will be parsed to Map<String, String>.
     * @param pagination not required, by default 'true' -> Boolean value
     * @param page       not required, by default '1' -> Integer value
     * @param size       not required, by default '10' -> Integer value
     * @param sortBy     not required, by default 'authorId' -> String value.
     * @param orderType  not required, by default 'asc' -> String value.
     * @return -> ResponseEntity with:
     *            array of Authors |
     *            empty array |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorsByParams(
            @RequestParam Map<String, String> params,
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        params.remove("page");
        params.remove("size");
        params.remove("pagination");

        params.putIfAbsent("sortBy", sortBy);
        params.putIfAbsent("orderType", orderType);

        List<AuthorDto> response = authorService.findFilteredAuthors(params, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new Author. Fields: 'authorId', 'authorName.first', 'authorName.second'
     * are mandatory. If field is skipped in JSON body it will assign empty string
     * for String type values and null for Date type.
     *
     * @param postAuthor required -> JSON body with new Author object
     * @return -> ResponseEntity with:
     *            created Author object |
     *            409 - Conflict |
     *            400 - Bad Request.
     */
    @PostMapping(value = "/author/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @param authorId      required -> Long value
     * @param updatedAuthor not required -> JSON body with Author object to update
     * @return -> ResponseEntity with:
     *            updated Author object |
     *            404 - Not Found |
     *            400 - Bad Request.
     */
    @PutMapping(value = "/author/{authorId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long authorId,
            @RequestBody @Valid AuthorDto updatedAuthor) {
        AuthorDto response = authorService.updateExistedAuthor(authorId, updatedAuthor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete existed Author by 'authorId'. If Author with such 'authorId' doesn't
     * exist it will produce 404 - Not Found. If Author has some related Books and
     * 'forcibly' is set as 'false' you will be informed, that Author has some Books.
     * If 'forcibly' indicator defining as 'true' it will delete Author and all related
     * Books.
     *
     * @param authorId required -> Long value
     * @param forcibly not required, by default 'false' -> Boolean value
     * @return -> ResponseEntity with:
     *            deleted Author object |
     *            404 - Author Not Found |
     *            400 - Bad Request.
     */
    @DeleteMapping(value = "/author/{authorId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAuthor(
            @PathVariable Long authorId,
            @RequestParam(name = "forcibly", defaultValue = "false") Boolean forcibly) {
        AuthorDto response = authorService.deleteExistedAuthor(authorId, forcibly);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
