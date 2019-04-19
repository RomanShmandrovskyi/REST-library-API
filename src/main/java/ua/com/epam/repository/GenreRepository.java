package ua.com.epam.repository;

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

    Optional<Genre> getOneByGenreId(long genreId);

    @Query(value = "SELECT g FROM Genre g")
    List<Genre> findAllOrderBy(Sort sort);

    @Query(value = "SELECT DISTINCT g.* FROM genre g JOIN book b ON g.genre_id = b.genre_id AND b.author_id = ?1", nativeQuery = true)
    List<Genre> getAllGenresOfAuthorByAuthorId(long authorId);
}
