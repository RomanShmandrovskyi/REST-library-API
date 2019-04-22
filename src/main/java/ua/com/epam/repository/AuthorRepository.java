package ua.com.epam.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByAuthorId(long authorId);

    Optional<Author> getOneByAuthorId(long authorId);

    @Query(value = "SELECT a FROM Author a")
    List<Author> findAllOrderBy(Sort sort);

    @Query(value = "SELECT DISTINCT a.* FROM author a JOIN book b ON a.author_id = b.author_id AND b.genre_id = ?1", nativeQuery = true)
    List<Author> getAllAuthorsOfGenreByGenreId(long genreId);

    @Query(value = "SELECT a.* FROM author AS a JOIN book AS b ON b.author_id = a.author_id AND book_id = ?1", nativeQuery = true)
    Author getAuthorOfBook(long bookId);
}
