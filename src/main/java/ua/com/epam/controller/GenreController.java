package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.entity.dto.genre.GenreDto;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.entity.exception.type.InvalidOrderTypeException;
import ua.com.epam.entity.exception.type.InvalidPageValueException;
import ua.com.epam.entity.exception.type.InvalidSizeValueException;
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

    private void checkPaginateParams(int page, int size) {
        if (page <= 0) {
            throw new InvalidPageValueException();
        }
        if (size < 0) {
            throw new InvalidSizeValueException();
        }
    }

    /**
     * Get Genre entity by genreId.
     *
     * @param genreId -> Long value
     * @return -> ResponseEntity with:
     *            Genre object |
     *            404 - Genre Not Found |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/genre/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGenre(
            @PathVariable Long genreId) {
        GenreDto response = genreService.findGenreByGenreId(genreId);
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
     * @param size      not required, by default - '5' -> Integer value
     * @return -> ResponseEntity with:
     *            paginated array of Genre objects |
     *            empty array |
     *            400 - Bad Request.
     */
    @GetMapping(value = "/genres",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGenres(
            @RequestParam(name = "pagination", defaultValue = "true") Boolean pagination,
            @RequestParam(name = "sortBy", defaultValue = "genreId") String sortBy,
            @RequestParam(name = "orderType", defaultValue = "asc") String orderType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        checkSortByKeyInGroup(sortBy);
        checkOrdering(orderType);
        checkPaginateParams(page, size);

        List<GenreDto> response = genreService.findAllGenres(sortBy, orderType, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new Genre. Fields: 'genreId' and 'genreName' are mandatory. If
     * field is skipped in JSON body it will assign empty string for String
     * type values.
     *
     * @param postGenre required -> JSON body with new Genre object
     * @return -> ResponseEntity with:
     *            new Genre object |
     *            409 - Conflict |
     *            400 - Bad Request.
     */
    @PostMapping(value = "/genre/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @return -> ResponseEntity with:
     *            updated Genre object |
     *            404 - Genre Not Found
     *            400 - Bad Request.
     */
    @PutMapping(value = "/genre/{genreId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @return ResponseEntity with:
     *         deleted Genre object |
     *         404 - Genre Not Found |
     *         400 - Bad Request.
     */
    @DeleteMapping(value = "/genre/{genreId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGenre(
            @PathVariable Long genreId,
            @RequestParam(name = "forcibly", defaultValue = "false") Boolean forcibly) {
        GenreDto response = genreService.deleteExistedGenre(genreId, forcibly);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
