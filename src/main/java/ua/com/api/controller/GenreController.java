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
import ua.com.api.entity.dto.genre.GenreDto;
import ua.com.api.entity.dto.genre.GenreWithoutIdDto;
import ua.com.api.exception.model.ExceptionResponse;
import ua.com.api.service.GenreService;
import ua.com.api.service.util.annotation.AllowableValues;

import javax.validation.constraints.Min;
import java.util.List;

import static ua.com.api.service.constants.Constants.*;

@RestController
@RequestMapping("${server.base.url}")
@Validated

@Tag(name = "Genre", description = "Genre table endpoints")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Operation(description = "get Genre object by 'genreId'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Special Genre object in JSON",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/genre/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenre(
            @Parameter(required = true, description = "existed Genre ID")
            @PathVariable
            Long genreId) {
        GenreDto response = genreService.findGenre(genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "get Genre of special Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre object of special Book in JSON",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "book/{bookId}/genre",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookGenre(
            @Parameter(required = true, description = "existed Book ID")
            @PathVariable
            Long bookId) {
        GenreDto response = genreService.findGenreOfBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get all Genres. Sorted by genre name only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Genre objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenreDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/genres",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGenres(
            @Parameter(description = "paginate response")
            @RequestParam(name = "pagination", defaultValue = TRUE)
            Boolean pagination,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of '" + ORDER_TYPE + "' parameter must be '" + ASC + "' or '" + DESC + "'")
            String orderType,

            @Parameter(description = "Custom sort parameter. Try '/genre/sortBy'")
            @RequestParam(name = SORT_BY, defaultValue = GENRE_ID)
            String sortBy,

            @Parameter(description = "page number")
            @RequestParam(name = PAGE, defaultValue = "1")
            @Min(value = 1, message = "Value of 'page' parameter must be positive and greater than zero!")
            Integer page,

            @Parameter(description = "count of objects per one page")
            @RequestParam(name = SIZE, defaultValue = DEFAULT_SIZE)
            @Min(value = 1, message = "Value of '" + SIZE + "' parameter must be positive and greater than zero!")
            Integer size) {

        List<GenreDto> response = genreService.findAllGenres(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "search for genre by it genre name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Genre objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenreDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/genres/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchForExistedGenres(
            @Parameter(description = "Searched query. At least 3 symbols exclude spaces in each word.", required = true)
            @RequestParam(name = QUERY)
            String query) {
        List<GenreDto> response = genreService.searchForExistedGenres(query);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "get all Genres of special Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Genre objects",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenreDto.class)))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping(value = "/author/{authorId}/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorGenres(
            @Parameter(required = true, description = "existed Author ID")
            @PathVariable
            Long authorId,

            @Schema(allowableValues = {ASC, DESC})
            @Parameter(description = "sorting order")
            @RequestParam(name = ORDER_TYPE, defaultValue = ASC)
            @AllowableValues(values = {ASC, DESC}, message = "Value of '" + ORDER_TYPE + "' parameter must be '" + ASC + "' or '" + DESC + "'")
            String orderType,

            @Parameter(description = "Custom sort parameter. Try '/genre/sortBy'")
            @RequestParam(name = SORT_BY, defaultValue = GENRE_ID)
            String sortBy) {

        List<GenreDto> response = genreService.findAllGenresOfAuthor(authorId, sortBy, orderType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/genre/sortBy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSortByValues() {
        List<SortByPropertiesDto> response = genreService.getSortByParameterValues();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "create new Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "newly created Genre",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Genre with such id already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/genre",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGenre(
            @Parameter(required = true, description = "Genre to add", name = "Genre object")
            @RequestBody @Validated
            GenreWithoutIdDto postGenre) {
        GenreDto response = genreService.addNewGenre(postGenre);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(description = "update existed Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated Genre object",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Genre to update not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping(value = "/genre",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGenre(
            @Parameter(required = true, description = "Genre to update", name = "Genre object")
            @RequestBody @Validated
            GenreDto updateGenre) {
        GenreDto response = genreService.updateExistedGenre(updateGenre);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "delete existed Genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Something wrong...",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Genre to delete not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/genre/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGenre(
            @Parameter(required = true, description = "existed Genre ID")
            @PathVariable
            Long genreId,

            @Parameter(description = "if false and Author has related Books, it will produce fault")
            @RequestParam(name = FORCIBLY, defaultValue = FALSE)
            Boolean forcibly) {
        genreService.deleteExistedGenre(genreId, forcibly);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
