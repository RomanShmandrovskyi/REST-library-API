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
import ua.com.api.entity.dto.SortByPropertiesDto;
import ua.com.api.entity.dto.author.AuthorDto;
import ua.com.api.entity.dto.author.AuthorWithoutIdDto;
import ua.com.api.exception.model.ExceptionResponse;
import ua.com.api.service.AuthorService;
import ua.com.api.service.util.annotation.AllowableValues;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ua.com.api.service.constants.Constants.*;

@Tag(name = "Author", description = "Author table endpoints")

@RestController
@RequestMapping("${server.base.url}")
@Validated
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get one Author object by it 'authorId'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Special Author object in JSON",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @Parameter(required = true, description = "existed Author ID")
            @PathVariable
            Long authorId) {
        AuthorDto response = authorService.findAuthor(authorId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get Author of Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author object of special Book",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/book/{bookId}/author", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorOfBook(
            @Parameter(required = true, description = "existed Book ID")
            @PathVariable
            Long bookId) {
        AuthorDto response = authorService.findAuthorOfBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get Authors with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Author objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(
            @Parameter(description = "use pagination or not")
            @RequestParam(name = PAGINATION, defaultValue = TRUE)
            Boolean pagination,

            @Parameter(description = "number of page")
            @RequestParam(name = PAGE, defaultValue = "1")
            @Min(value = 1, message = "Value of 'page' parameter must be positive and greater than zero!")
            Integer page,

            @Parameter(description = "count of objects per page")
            @RequestParam(name = SIZE, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = "Value of 'size' parameter must be positive and greater than zero!")
            Integer size,

            @Parameter(description = "custom sort parameter, try '/author/sorters' endpoint")
            @RequestParam(name = SORT_BY, defaultValue = AUTHOR_ID)
            String sortBy,

            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of " + ORDER_TYPE + " parameter must be '" + ASC + "' or '" + DESC + "'")
            String orderType) {
        List<AuthorDto> response = authorService.findAllAuthors(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Search for Author by it name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Authors objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/authors/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchForAuthors(
            @Parameter(description = "Searched query. At least 3 symbols exclude spaces in each word.", required = true)
            @RequestParam(name = QUERY)
            String query) {
        List<AuthorDto> response = authorService.searchForExistedAuthors(query);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get Authors that writes in special Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Authors objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/genre/{genreId}/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorsInGenre(
            @Parameter(description = "existed Genre ID", required = true)
            @PathVariable
            Long genreId,

            @Parameter(description = "use pagination or not")
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

            @Parameter(description = "Custom sort parameter. Try '/author/sortBy' endpoint")
            @RequestParam(name = SORT_BY, defaultValue = AUTHOR_ID)
            String sortBy,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of 'orderType' parameter must be '" + ASC + "' or '" + DESC + "'")
            String orderType) {
        List<AuthorDto> response = authorService.findAllAuthorsInGenre(genreId, sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get available sorters for Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "array with 'sortBy' parameter values and some aliases",
                    content = @Content(schema = @Schema(implementation = SortByPropertiesDto.class)))
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/author/sorters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSortByValues() {
        List<SortByPropertiesDto> response = authorService.getSortByParameterValues();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Create new Author", description = "Author must have unique first and last names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "newly created Author",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Author with such id already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/author", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewAuthor(
            @Parameter(required = true, description = "Author to add", name = "Author object")
            @Valid @RequestBody AuthorWithoutIdDto postAuthor) {
        AuthorDto response = authorService.addNewAuthor(postAuthor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Update existed Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated Author object",
                    content = @Content(schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author to update not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping(value = "/author/{authorId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @Parameter(description = "existed Author ID", required = true)
            @PathVariable
            Long authorId,

            @Parameter(required = true, description = "Author to update", name = "Author object")
            @Valid @RequestBody AuthorWithoutIdDto updatedAuthor) {
        AuthorDto response = authorService.updateExistedAuthor(authorId, updatedAuthor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Remove existed Author with it books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author to delete not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAuthor(
            @Parameter(description = "existed Author ID", required = true)
            @PathVariable
            Long authorId,

            @Schema(allowableValues = {FALSE, TRUE})
            @Parameter(description = "if 'false' and Author has related Books - will produce a fault, if 'true' - will delete also all Books of this Author")
            @RequestParam(name = FORCIBLY, defaultValue = FALSE)
            Boolean forcibly) {
        authorService.deleteExistedAuthor(authorId, forcibly);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}