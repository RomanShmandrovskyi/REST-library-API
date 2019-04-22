package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithAuthorsDto;
import ua.com.epam.entity.dto.genre.SimpleGenreWithBooksDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidOrderTypeException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/library")
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

    /**
     * Get Genre entity by genreId.
     *
     * @param genreId -> Long value
     * @return -> ResponseEntity with Genre object or 404 - Genre Not Found
     */
    @GetMapping(value = "/genre/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenre(
            @PathVariable Long genreId) {
        GenreDto response = genreService.findGenreByGenreId(genreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Find all Authors of Genre. Will return simple info about Genre ('genreId'
     * and 'genreName') with array of Author objects that will contain next
     * values: 'authorId', 'authorName.first' and 'authorName.second'. Authors
     * array size can be limited using 'limit' query param.
     *
     * @param genreId      required -> Long value
     * @param authorsCount not required, by default = '5' -> Integer value
     * @return -> ResponseEntity with Simple Genre object with limited Authors in it as array
     */
    @GetMapping(value = "/genre/{genreId}/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenreAuthors(
            @PathVariable Long genreId,
            @RequestParam(name = "limit", defaultValue = "5") Integer authorsCount) {
        SimpleGenreWithAuthorsDto response = genreService.getGenreWithItAuthors(genreId, authorsCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Find all Books of Genre. Will return simple info about Genre ('genreId'
     * and 'genreName') with array of Book objects that will contain next
     * values: 'bookId' and 'bookName'. Books array size can be limited using
     * 'limit' query param.
     *
     * @param genreId    required -> Long value
     * @param booksCount not required, by default = '5' -> Integer value
     * @return -> ResponseEntity with Simple Genre object with limited Books in it as array
     */
    @GetMapping(value = "/genre/{genreId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenreBooks(
            @PathVariable Long genreId,
            @RequestParam(name = "limit", defaultValue = "5") Integer booksCount) {
        SimpleGenreWithBooksDto response = genreService.getGenreWithItBooks(genreId, booksCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get array of existed Genre Objects. Can sort by any other one json
     * key. If key not exists in JSON, will be thrown exception. By default
     * sort in ascending order. Descending order is available too. This
     * endpoint can also paginate response, just set page number to 'page'
     * param and needed entities count on one page in 'size' param. Any
     * others query params (expect 'sortBy', 'orderType', 'page' and 'size')
     * will be ignored.
     *
     * @param sortBy    not required, by default - 'genreId' -> Long value
     * @param orderType not required, by default - 'asc' -> String value
     * @param page      not required, by default - '1' -> Integer value
     * @param size      not required, by default - '3' -> Integer value
     * @return -> ResponseEntity with Array of paginated array of Genres
     */
    @GetMapping(value = "/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGenres(
            @RequestParam(name = "sortBy", defaultValue = "genreId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "3") Integer size) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);

        List<GenreDto> response = genreService.getAllGenres(sortBy, orderType, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new Genre. Fields: 'genreId' and 'genreName' are mandatory. If
     * field is skipped in JSON body it will assign empty string for String
     * type values.
     *
     * @param postGenre required -> JSON body with new Genre object
     * @return -> ResponseEntity with new Genre or 409 - Conflict
     */
    @PostMapping(value = "/genre/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGenre(
            @RequestBody @Valid GenreDto postGenre) {
        GenreDto response = genreService.addNewGenre(postGenre);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existed Genre. Consume full object with updated JSON fields.
     * Path param 'genreId' must be the same as in body to update. In other
     * way will be thrown exception.
     *
     * @param genreId     required -> Long value
     * @param updateGenre required -> JSON body with Genre object to update
     * @return -> ResponseEntity with updated Genre or 404 - Genre Not Found
     */
    @PutMapping(value = "/genre/{genreId}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long genreId,
            @RequestBody @Valid GenreDto updateGenre) {
        GenreDto response = genreService.updateExistedGenre(genreId, updateGenre);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete existed Genre by 'genreId'. If Genre with such 'genreId' doesn't exist
     * it will produce 404 - Genre Not Found. If Genre has some related Books and
     * 'forcibly' is set as 'false' you will be informed, that Author has some Books.
     * If 'forcibly' indicator defining as 'true' it will delete Genre and all related
     * Books.
     *
     * @param genreId  required -> Long value
     * @param forcibly not required, by default 'false' -> Boolean value
     * @return ResponseEntity with deleted Genre or 404 - Genre Not Found
     */
    @DeleteMapping(value = "/genre/{genreId}/delete")
    public ResponseEntity<?> deleteGenre(
            @PathVariable Long genreId,
            @RequestParam(name = "forcibly", defaultValue = "false") Boolean forcibly) {
        GenreDto response = genreService.deleteExistedGenre(genreId, forcibly);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
