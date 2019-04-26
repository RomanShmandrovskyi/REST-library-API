package ua.com.epam.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.epam.entity.GroupByBooksCount;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupByBooksRepository extends JpaRepository<GroupByBooksCount, Long> {

    @Query(value = "SELECT NEW GroupByBooksCount(g.genreId, g.genreName, COUNT(g.genreId)) FROM Genre g JOIN Book b ON g.genreId = b.genreId AND g.genreId = ?1 GROUP BY g.genreId")
    Optional<GroupByBooksCount> getGenreGroupByBooks(long genreId);

    @Query(value = "SELECT NEW GroupByBooksCount(g.genreId, g.genreName, COUNT(g.genreId)) FROM Genre g JOIN Book b ON g.genreId = b.genreId GROUP BY g.genreId")
    List<GroupByBooksCount> getAllGenresGroupByBooks(PageRequest pageRequest);

    @Query(value = "SELECT NEW GroupByBooksCount(a.authorId, CONCAT(a.firstName, ' ', a.secondName), COUNT(a.authorId)) FROM Author a JOIN Book b ON a.authorId = b.authorId AND a.authorId = ?1 GROUP BY a.authorId")
    Optional<GroupByBooksCount> getAuthorGroupByBooks(long authorId);

    @Query(value = "SELECT NEW GroupByBooksCount(a.authorId, CONCAT(a.firstName, ' ', a.secondName), COUNT(a.authorId)) FROM Author a JOIN Book b ON a.authorId = b.authorId GROUP BY a.authorId ORDER BY COUNT(a.authorId) ASC")
    List<GroupByBooksCount> getAllAuthorsGroupByBooks();

    @Query(value = "SELECT NEW GroupByBooksCount(a.authorId, CONCAT(a.firstName, ' ', a.secondName), COUNT(a.authorId)) FROM Author a JOIN Book b ON a.authorId = b.authorId GROUP BY a.authorId ORDER BY COUNT(a.authorId) ASC")
    List<GroupByBooksCount> getAllAuthorsGroupByBooksPaginated(PageRequest pageRequest);
}