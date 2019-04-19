package ua.com.epam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> getBookByBookId(long bookId);

    @Query(value = "SELECT * FROM library.book AS b WHERE b.author_id = ?1", nativeQuery = true)
    List<Book> getAuthorBooksByAuthorId(long authorId);

    @Query(value = "SELECT * FROM library.book AS b WHERE b.genre_id = ?1", nativeQuery = true)
    List<Book> getGenreBooksByGenreId(long genreId);
}