package ua.com.epam.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.epam.entity.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Genre getGenreByGenreId(long genreId);
}
