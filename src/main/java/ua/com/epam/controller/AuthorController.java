package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.epam.assembler.AuthorAssembler;

import java.util.Map;

@RestController
@RequestMapping("/api/library")
public class AuthorController {

    @Autowired
    private AuthorAssembler authorAssembler;

    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthor(
            @PathVariable long authorId) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorsByParams(
            @RequestParam Map<String, String> params,
            @RequestParam(name = "count", defaultValue = "3") int count,
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy) {
        String result = authorAssembler.getAuthorsWithFilters(params, sortBy, count);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Return all Authors in JSON
     * @param sortBy string value of key by sort to (by default = 'authorId')
     * @return ResponseEntity
     */
    @GetMapping(value = "/authors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(
            @RequestParam(name = "sortBy", defaultValue = "authorId") String sortBy) {
        String response = authorAssembler.getAllAuthorsSortedBy(sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
