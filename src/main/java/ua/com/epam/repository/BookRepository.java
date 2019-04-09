package ua.com.epam.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.epam.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book getBookByBookId(long bookId);
}
