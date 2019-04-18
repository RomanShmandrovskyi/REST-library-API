package ua.com.epam.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.epam.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByAuthorId(long authorId);

    Optional<Author> getOneByAuthorId(long authorId);

    @Query(value = "SELECT a FROM Author a")
    List<Author> findAllOrderBy(Sort sort);

    @Modifying
    @Transactional
    void deleteByAuthorId(long authorId);
}
