package ua.com.api.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.api.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> getOneByBookId(long bookId);

    boolean existsByBookId(long bookId);

    boolean existsByBookNameAndBookDescription(String bookName, String bookDescription);

    boolean existsByBookNameAndBookDescriptionAndBookIdNotLike(String bookName, String bookDescription, long bookId);

    @Query(value = "SELECT b FROM Book b")
    List<Book> getAllBooks(PageRequest page);

    @Query(value = "SELECT COUNT(b) FROM Book b WHERE b.genre.genreId=?1")
    Long getAllBooksInGenreCount(long genreId);

    @Query(value = "SELECT COUNT(b) FROM Book b WHERE b.author.authorId=?1")
    Long getAllBooksOfAuthorCount(long authorId);

    @Query(value = "SELECT b FROM Book b WHERE b.genre.genreId=?1")
    List<Book> getAllBooksInGenre(long genreId, PageRequest page);

    @Query(value = "SELECT b FROM Book b WHERE b.author.authorId=?1")
    List<Book> getAllAuthorBooksOrdered(long authorId, Sort sort);

    @Query(value = "SELECT b FROM Book b WHERE b.author.authorId=?1 AND b.genre.genreId=?2")
    List<Book> getAllAuthorBooksInGenre(long authorId, long genreId);
}