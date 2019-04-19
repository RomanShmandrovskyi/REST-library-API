package ua.com.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.epam.entity.exception.NoSuchJsonKeyException;
import ua.com.epam.repository.JsonKeysConformity;
import ua.com.epam.service.GenreService;

@RestController
@RequestMapping("/api/library")
public class GenreController {

    @Autowired
    private GenreService genreService;

    private void checkSortByKeyInGroup(String sortBy) {
        if (!JsonKeysConformity.ifJsonKeyExistsInGroup(sortBy, JsonKeysConformity.Group.GENRE)) {
            throw new NoSuchJsonKeyException(sortBy);
        }
    }


}
