package ua.com.epam.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    boolean existsByGenreId(long genreId);

    boolean existsByGenreName(String genreName);

    Optional<Genre> getOneByGenreId(long genreId);

    @Query(value = "SELECT g FROM Genre g")
    List<Genre> getAllGenres(PageRequest page);

    @Query(value = "SELECT DISTINCT g FROM Genre g JOIN Book b ON g.genreId = b.genreId AND b.authorId = ?1")
    List<Genre> getAllGenresOfAuthorOrdered(long authorId, Sort sort);

    @Query(value = "SELECT g FROM Genre g JOIN Book b ON b.genreId = g.genreId AND b.bookId = ?1")
    Genre getGenreOfBook(long bookId);
}
