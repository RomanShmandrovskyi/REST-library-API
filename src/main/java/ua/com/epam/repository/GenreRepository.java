package ua.com.epam.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Genre;

import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {

    Optional<Genre> getGenreByGenreId(long genreId);
}
