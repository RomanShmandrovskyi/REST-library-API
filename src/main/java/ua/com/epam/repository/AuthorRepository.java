package ua.com.epam.repository;

import org.springframework.data.domain.PageRequest;
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

    @Query(value = "SELECT a FROM Author a JOIN Book b ON b.authorId = a.authorId AND b.bookId = ?1")
    Author getAuthorOfBook(long bookId);

    @Query(value = "SELECT a FROM Author a")
    List<Author> getAllAuthors(PageRequest page);

    @Query(value = "SELECT DISTINCT a FROM Author a JOIN Book b ON a.authorId = b.authorId AND b.genreId = ?1")
    List<Author> getAllAuthorsInGenre(long genreId, PageRequest page);
}
