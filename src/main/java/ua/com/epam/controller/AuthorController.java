package ua.com.epam.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.author.AuthorDto;
import ua.com.epam.exception.entity.NoSuchJsonKeyException;
import ua.com.epam.exception.entity.type.InvalidOrderTypeException;
import ua.com.epam.exception.entity.type.InvalidPageValueException;
import ua.com.epam.exception.entity.type.InvalidSizeValueException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@Api(value = "Author", description = "Operations with Author in Library", tags = { "Author" })
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
        if (size <= 0) {
            throw new InvalidSizeValueException();
        }
    }

    @ApiOperation(value = "get Author object by 'authorId'", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Special Author object in JSON", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Author not found")
    })
    @GetMapping(value = "/author/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @ApiParam(required = true, value = "existed Author ID")
            @PathVariable Long authorId) {
        AuthorDto response = authorService.findAuthorByAuthorId(authorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get Author of special Book", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author object of special Book in JSON", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @GetMapping(value = "/book/{bookId}/author",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorOfBook(
            @ApiParam(required = true, value = "existed Book ID")
            @PathVariable Long bookId) {
        AuthorDto response = authorService.findAuthorOfBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Authors", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Array of Authors objects",
                    responseContainer = "Set", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong...")
    })
    @GetMapping(value = "/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(
            @ApiParam(value = "paginate response")
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @ApiParam(value = "page number")
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @ApiParam(value = "count of objects on one page")
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "custom sort parameter")
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy,
            @ApiParam(allowableValues = "asc,desc", value = "sorting order")
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<AuthorDto> response = authorService.findAllAuthors(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Authors in special Genre", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Array of Authors objects",
                    responseContainer = "Set", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Genre not found")
    })
    @GetMapping(value = "/genre/{genreId}/authors",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorsOfGenre(
            @ApiParam(required = true, value = "existed Genre ID")
            @PathVariable Long genreId,
            @ApiParam(value = "paginate response")
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @ApiParam(value = "page number")
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @ApiParam(value = "count of objects on one page")
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @ApiParam(value = "custom sort parameter")
            @RequestParam(name = "sortBy") String sortBy,
            @ApiParam(allowableValues = "asc,desc", value = "sorting order")
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<AuthorDto> response = authorService.findAllAuthorsOfGenre(genreId, sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "create new Author", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "newly created Author", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 409, message = "Author with such id already exists")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/author/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewAuthor(
            @RequestBody @Valid AuthorDto postAuthor) {
        AuthorDto response = authorService.addNewAuthor(postAuthor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update existed Author", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "updated Author object", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Author to update not found")
    })
    @PutMapping(value = "/author/{authorId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @ApiParam(required = true, value = "existed Author ID")
            @PathVariable Long authorId,
            @RequestBody @Valid AuthorDto updatedAuthor) {
        AuthorDto response = authorService.updateExistedAuthor(authorId, updatedAuthor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "delete existed Author", tags = { "Author" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "deleted Author object", response = AuthorDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Author to delete not found")
    })
    @DeleteMapping(value = "/author/{authorId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAuthor(
            @ApiParam(required = true, value = "existed Author ID")
            @PathVariable Long authorId,
            @ApiParam(value = "if false and Author has related Books, it will produce fault")
            @RequestParam(name = "forcibly", defaultValue = "false") Boolean forcibly) {
        AuthorDto response = authorService.deleteExistedAuthor(authorId, forcibly);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}