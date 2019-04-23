package ua.com.epam.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> getOneByBookId(long bookId);

    boolean existsByBookId(long bookId);

    @Query(value = "SELECT b FROM Book b")
    List<Book> getAllBooksOrdered(Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?")
    List<Book> getAllAuthorBooks(long authorId);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?")
    List<Book> getAllAuthorBooksOrdered(long authorId, Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.genreId=?")
    List<Book> getAllBooksInGenre(long genreId);

    @Query(value = "SELECT b FROM Book b WHERE b.genreId=?")
    List<Book> getAllBooksInGenreOrdered(long genreId, Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?1 and b.genreId=?2")
    List<Book> getAllAuthorBooksInGenre(long authorId, long genreId);
}