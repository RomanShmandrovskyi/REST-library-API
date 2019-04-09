package ua.com.epam.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.epam.entity.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author getAuthorByAuthorId(long authorId);
}
