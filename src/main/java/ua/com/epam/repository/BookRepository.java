package ua.com.epam.repository;

import org.springframework.data.domain.PageRequest;
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
    List<Book> getAllBooksOrderedPaginated(PageRequest page);

    @Query(value = "SELECT b FROM Book b WHERE b.genreId=?1")
    List<Book> getAllBooksInGenreOrderedPaginated(long genreId, PageRequest page);

    @Query(value = "SELECT b FROM Book b WHERE b.genreId=?1")
    List<Book> getAllBooksInGenre(long genreId);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?1")
    List<Book> getAllAuthorBooks(long authorId);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?1")
    List<Book> getAllAuthorBooksOrdered(long authorId, Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.authorId=?1 AND b.genreId=?2")
    List<Book> getAllAuthorBooksInGenre(long authorId, long genreId);

    @Query(value = "SELECT b FROM Book b ORDER BY b.bookHeight * b.bookWidth * b.bookLength")
    List<Book> getAllBooksOrderedByVolumePaginated(PageRequest request);

    @Query(value = "SELECT b FROM Book b ORDER BY b.bookWidth * b.bookLength")
    List<Book> getAllBooksOrderedBySquarePaginated(PageRequest request);
}