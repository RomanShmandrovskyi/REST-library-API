package ua.com.epam.exception.entity.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BooksInAuthorArePresentException extends RuntimeException {
    private long authorId;
    private long booksCount;
}
