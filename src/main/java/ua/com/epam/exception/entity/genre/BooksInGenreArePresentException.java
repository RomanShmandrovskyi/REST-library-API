package ua.com.epam.exception.entity.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksInGenreArePresentException extends RuntimeException {
    private long genreId;
    private long booksCount;
}