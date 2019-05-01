package ua.com.epam.exception.entity.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookNotFoundException extends RuntimeException {
    private long bookId;
}
