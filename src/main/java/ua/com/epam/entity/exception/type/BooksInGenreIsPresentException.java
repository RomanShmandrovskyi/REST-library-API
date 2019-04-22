package ua.com.epam.entity.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksInGenreIsPresentException extends RuntimeException {
    private long genreId;
    private long booksCount;
}