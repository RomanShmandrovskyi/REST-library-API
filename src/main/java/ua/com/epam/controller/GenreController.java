package ua.com.epam.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.exception.entity.NoSuchJsonKeyException;
import ua.com.epam.exception.entity.type.InvalidOrderTypeException;
import ua.com.epam.exception.entity.type.InvalidPageValueException;
import ua.com.epam.exception.entity.type.InvalidSizeValueException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${server.base.url}")
@Api(value = "Genre", description = "Genre endpoints", tags = {"Genre"})
public class GenreController {

    @Autowired
    private GenreService genreService;

    private void checkOrdering(String orderType) {
        if (!orderType.equals("asc") && !orderType.equals("desc")) {
            throw new InvalidOrderTypeException(orderType);
        }
    }

    private void checkSortByKeyInGroup(String sortBy) {
        if (!JsonKeysConformity.ifJsonKeyExistsInGroup(sortBy, JsonKeysConformity.Group.GENRE)) {
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

    @ApiOperation(value = "get Genre object by 'genreId'", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Special Genre object in JSON", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Genre not found")
    })
    @GetMapping(value = "/genre/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenre(
            @ApiParam(required = true, value = "existed Genre ID")
            @PathVariable
                    Long genreId) {
        GenreDto response = genreService.findGenre(genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get Genre of special Book", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Genre object of special Book in JSON", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @GetMapping(value = "book/{bookId}/genre",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookGenre(
            @ApiParam(required = true, value = "existed Book ID")
            @PathVariable
                    Long bookId) {
        GenreDto response = genreService.findGenreOfBook(bookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Genres", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Array of Genre objects",
                    responseContainer = "Set", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong...")
    })
    @GetMapping(value = "/genres",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGenres(
            @ApiParam(value = "paginate response")
            @RequestParam(name = "pagination", defaultValue = "true")
                    Boolean pagination,

            @ApiParam(value = "custom sort parameter")
            @RequestParam(name = "sortBy", defaultValue = "genreId")
                    String sortBy,

            @ApiParam(allowableValues = "asc,desc", value = "sorting order")
            @RequestParam(name = "orderType", defaultValue = "asc")
                    String orderType,

            @ApiParam(value = "page number")
            @RequestParam(name = "page", defaultValue = "1")
                    Integer page,

            @ApiParam(value = "count of objects per one page")
            @RequestParam(name = "size", defaultValue = "10")
                    Integer size) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<GenreDto> response = genreService.findAllGenres(sortBy, orderType, page, size, pagination);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "search for genre by it genre name", tags = "Genre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Array of Genre objects",
                    responseContainer = "Set", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong...")
    })
    @GetMapping(value = "/genres/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchForExistedGenres(
            @ApiParam(value = "Searched query. At least 3 symbols exclude spaces in each word.", required = true)
            @RequestParam(name = "query")
                    String query) {
        List<GenreDto> response = genreService.searchForExistedGenres(query);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Genres of special Author", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Array of Genre objects",
                    responseContainer = "Set", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Author not found")
    })
    @GetMapping(value = "/author/{authorId}/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthorGenres(
            @ApiParam(required = true, value = "existed Author ID")
            @PathVariable
                    Long authorId,

            @ApiParam(value = "custom sort parameter")
            @RequestParam(name = "sortBy", defaultValue = "genreId")
                    String sortBy,

            @ApiParam(allowableValues = "asc,desc", value = "sorting order")
            @RequestParam(name = "orderType", defaultValue = "asc")
                    String orderType) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<GenreDto> response = genreService.findAllGenresOfAuthor(authorId, sortBy, orderType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "create new Genre", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "newly created Genre", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 409, message = "Genre with such id already exists")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/genre",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGenre(
            @ApiParam(required = true, value = "Genre to add", name = "Genre object")
            @RequestBody @Valid
                    GenreDto postGenre) {
        GenreDto response = genreService.addNewGenre(postGenre);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update existed Genre", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "updated Genre object", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Genre to update not found")
    })
    @PutMapping(value = "/genre",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGenre(
            @ApiParam(required = true, value = "Genre to update", name = "Genre object")
            @RequestBody @Valid
                    GenreDto updateGenre) {
        GenreDto response = genreService.updateExistedGenre(updateGenre);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "delete existed Genre", tags = {"Genre"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Genre deleted successfully"),
            @ApiResponse(code = 400, message = "Something wrong..."),
            @ApiResponse(code = 404, message = "Genre to delete not found")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/genre/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGenre(
            @ApiParam(required = true, value = "existed Genre ID")
            @PathVariable
                    Long genreId,

            @ApiParam(value = "if false and Author has related Books, it will produce fault")
            @RequestParam(name = "forcibly", defaultValue = "false")
                    Boolean forcibly) {
        genreService.deleteExistedGenre(genreId, forcibly);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
